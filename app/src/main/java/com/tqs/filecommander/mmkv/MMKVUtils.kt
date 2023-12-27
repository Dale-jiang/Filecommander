package com.tqs.filecommander.mmkv

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface IMMKVOwner {
    val mapId: String
    val kv: MMKV
}

open class MMKVOwner(override val mapId: String) : IMMKVOwner {
    override val kv: MMKV by lazy { MMKV.mmkvWithID(mapId, MMKV.MULTI_PROCESS_MODE) }
}

fun IMMKVOwner.mmkvInt(default: Int = 0) = MMKVProperty({ kv.decodeInt(it, default) }, { kv.encode(first, second) })
fun IMMKVOwner.mmkvLong(default: Long = 0L) = MMKVProperty({ kv.decodeLong(it, default) }, { kv.encode(first, second) })
fun IMMKVOwner.mmkvBoolean(default: Boolean = false) = MMKVProperty({ kv.decodeBool(it, default) }, { kv.encode(first, second) })
fun IMMKVOwner.mmkvFloat(default: Float = 0f) = MMKVProperty({ kv.decodeFloat(it, default) }, { kv.encode(first, second) })
fun IMMKVOwner.mmkvDouble(default: Double = 0.0) = MMKVProperty({ kv.decodeDouble(it, default) }, { kv.encode(first, second) })
fun IMMKVOwner.mmkvString() = MMKVProperty({ kv.decodeString(it) }, { kv.encode(first, second) })
fun IMMKVOwner.mmkvString(default: String) = MMKVProperty({ kv.decodeString(it, default) }, { kv.encode(first, second) })
fun IMMKVOwner.mmkvStringSet() = MMKVProperty({ kv.decodeStringSet(it) }, { kv.encode(first, second) })
fun IMMKVOwner.mmkvStringSet(default: Set<String>) = MMKVProperty({ kv.decodeStringSet(it, default) }, { kv.encode(first, second) })
fun IMMKVOwner.mmkvBytes() = MMKVProperty({ kv.decodeBytes(it) }, { kv.encode(first, second) })
fun IMMKVOwner.mmkvBytes(default: ByteArray) = MMKVProperty({ kv.decodeBytes(it, default) }, { kv.encode(first, second) })
inline fun <reified T : Parcelable> IMMKVOwner.mmkvParcelable() =
    MMKVProperty({ kv.decodeParcelable(it, T::class.java) }, { kv.encode(first, second) })

inline fun <reified T : Parcelable> IMMKVOwner.mmkvParcelable(default: T) =
    MMKVProperty({ kv.decodeParcelable(it, T::class.java) }, { kv.encode(first, second) })

fun <V> MMKVProperty<V>.asLiveData() = object : ReadOnlyProperty<IMMKVOwner, MutableLiveData<V>> {
    private var cache: MutableLiveData<V>? = null
    override fun getValue(thisRef: IMMKVOwner, property: KProperty<*>): MutableLiveData<V> =
        cache ?: object : MutableLiveData<V>() {
            override fun getValue() = this@asLiveData.getValue(thisRef, property)

            override fun setValue(value: V) {
                if (super.getValue() == value) return
                this@asLiveData.setValue(thisRef, property, value)
                super.setValue(value)
            }

            override fun onActive() = super.setValue(value)
        }.also { cache = it }
}


class MMKVProperty<v>(
    private val decode: (String) -> v,
    private val encode: Pair<String, v>.() -> Boolean,
    var key: String? = null
) : ReadWriteProperty<IMMKVOwner, v> {
    override fun getValue(thisRef: IMMKVOwner, property: KProperty<*>): v =
        decode(key ?: property.name)


    override fun setValue(thisRef: IMMKVOwner, property: KProperty<*>, value: v) {
        encode((key ?: property.name) to value)
    }


}

