package engine.registries

interface IRegistry<TKey, TValue> {
    fun getValue(key: TKey): TValue?
    fun setValue(key: TKey, value: TValue?)
}