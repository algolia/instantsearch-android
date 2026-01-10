package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object Languages {
    const val Afrikaans: String = "af"
    const val Arabic: String = "ar"
    const val Azeri: String = "az"
    const val Bulgarian: String = "bg"
    const val Brunei: String = "bn"
    const val Catalan: String = "ca"
    const val Czech: String = "cs"
    const val Welsh: String = "cy"
    const val Danish: String = "da"
    const val German: String = "de"
    const val English: String = "en"
    const val Esperanto: String = "eo"
    const val Spanish: String = "es"
    const val Estonian: String = "et"
    const val Basque: String = "eu"
    const val Finnish: String = "fi"
    const val Faroese: String = "fo"
    const val French: String = "fr"
    const val Galician: String = "gl"
    const val Hebrew: String = "he"
    const val Hindi: String = "hi"
    const val Hungarian: String = "hu"
    const val Armenian: String = "hy"
    const val Indonesian: String = "id"
    const val Icelandic: String = "is"
    const val Italian: String = "it"
    const val Japanese: String = "ja"
    const val Georgian: String = "ka"
    const val Kazakh: String = "kk"
    const val Korean: String = "ko"
    const val Kyrgyz: String = "ky"
    const val Lithuanian: String = "lt"
    const val Maori: String = "mi"
    const val Mongolian: String = "mn"
    const val Marathi: String = "mr"
    const val Malay: String = "ms"
    const val Maltese: String = "mt"
    const val Norwegian: String = "nb"
    const val Dutch: String = "nl"
    const val NorthernSotho: String = "ns"
    const val Polish: String = "pl"
    const val Pashto: String = "ps"
    const val Portuguese: String = "pt"
    const val Quechua: String = "qu"
    const val Romanian: String = "ro"
    const val Russian: String = "ru"
    const val Slovak: String = "sk"
    const val Albanian: String = "sq"
    const val Swedish: String = "sv"
    const val Swahili: String = "sw"
    const val Tamil: String = "ta"
    const val Telugu: String = "te"
    const val Tagalog: String = "tl"
    const val Tswana: String = "tn"
    const val Turkish: String = "tr"
    const val Tatar: String = "tt"
}


@Serializable(Language.Companion::class)
public sealed class Language(override val raw: String) : Raw<String> {

    public object Afrikaans : Language(Languages.Afrikaans)
    public object Arabic : Language(Languages.Arabic)
    public object Azeri : Language(Languages.Azeri)
    public object Bulgarian : Language(Languages.Bulgarian)
    public object Brunei : Language(Languages.Brunei)
    public object Catalan : Language(Languages.Catalan)
    public object Czech : Language(Languages.Czech)
    public object Welsh : Language(Languages.Welsh)
    public object Danish : Language(Languages.Danish)
    public object German : Language(Languages.German)
    public object English : Language(Languages.English)
    public object Esperanto : Language(Languages.Esperanto)
    public object Spanish : Language(Languages.Spanish)
    public object Estonian : Language(Languages.Estonian)
    public object Basque : Language(Languages.Basque)
    public object Finnish : Language(Languages.Finnish)
    public object Faroese : Language(Languages.Faroese)
    public object French : Language(Languages.French)
    public object Galician : Language(Languages.Galician)
    public object Hebrew : Language(Languages.Hebrew)
    public object Hindi : Language(Languages.Hindi)
    public object Hungarian : Language(Languages.Hungarian)
    public object Armenian : Language(Languages.Armenian)
    public object Indonesian : Language(Languages.Indonesian)
    public object Icelandic : Language(Languages.Icelandic)
    public object Italian : Language(Languages.Italian)
    public object Japanese : Language(Languages.Japanese)
    public object Georgian : Language(Languages.Georgian)
    public object Kazakh : Language(Languages.Kazakh)
    public object Korean : Language(Languages.Korean)
    public object Kyrgyz : Language(Languages.Kyrgyz)
    public object Lithuanian : Language(Languages.Lithuanian)
    public object Maori : Language(Languages.Maori)
    public object Mongolian : Language(Languages.Mongolian)
    public object Marathi : Language(Languages.Marathi)
    public object Malay : Language(Languages.Malay)
    public object Maltese : Language(Languages.Maltese)
    public object Norwegian : Language(Languages.Norwegian)
    public object Dutch : Language(Languages.Dutch)
    public object NorthernSotho : Language(Languages.NorthernSotho)
    public object Polish : Language(Languages.Polish)
    public object Pashto : Language(Languages.Pashto)
    public object Portuguese : Language(Languages.Portuguese)
    public object Quechua : Language(Languages.Quechua)
    public object Romanian : Language(Languages.Romanian)
    public object Russian : Language(Languages.Russian)
    public object Slovak : Language(Languages.Slovak)
    public object Albanian : Language(Languages.Albanian)
    public object Swedish : Language(Languages.Swedish)
    public object Swahili : Language(Languages.Swahili)
    public object Tamil : Language(Languages.Tamil)
    public object Telugu : Language(Languages.Telugu)
    public object Tagalog : Language(Languages.Tagalog)
    public object Tswana : Language(Languages.Tswana)
    public object Turkish : Language(Languages.Turkish)
    public object Tatar : Language(Languages.Tatar)

    public data class Other(override val raw: String) : Language(raw)

    override fun toString(): String {
        return raw
    }

    public companion object : KSerializer<Language> {

        private val serializer = String.serializer()

        override val descriptor: SerialDescriptor = serializer.descriptor

        override fun serialize(encoder: Encoder, value: Language) {
            serializer.serialize(encoder, value.raw)
        }

        override fun deserialize(decoder: Decoder): Language {
            return when (val string = serializer.deserialize(decoder)) {
                Languages.Afrikaans -> Afrikaans
                Languages.Arabic -> Arabic
                Languages.Azeri -> Azeri
                Languages.Bulgarian -> Bulgarian
                Languages.Brunei -> Brunei
                Languages.Catalan -> Catalan
                Languages.Czech -> Czech
                Languages.Welsh -> Welsh
                Languages.Danish -> Danish
                Languages.German -> German
                Languages.English -> English
                Languages.Esperanto -> Esperanto
                Languages.Spanish -> Spanish
                Languages.Estonian -> Estonian
                Languages.Basque -> Basque
                Languages.Finnish -> Finnish
                Languages.Faroese -> Faroese
                Languages.French -> French
                Languages.Galician -> Galician
                Languages.Hebrew -> Hebrew
                Languages.Hindi -> Hindi
                Languages.Hungarian -> Hungarian
                Languages.Armenian -> Armenian
                Languages.Indonesian -> Indonesian
                Languages.Icelandic -> Icelandic
                Languages.Italian -> Italian
                Languages.Japanese -> Japanese
                Languages.Georgian -> Georgian
                Languages.Kazakh -> Kazakh
                Languages.Korean -> Korean
                Languages.Kyrgyz -> Kyrgyz
                Languages.Lithuanian -> Lithuanian
                Languages.Maori -> Maori
                Languages.Mongolian -> Mongolian
                Languages.Marathi -> Marathi
                Languages.Malay -> Malay
                Languages.Maltese -> Maltese
                Languages.Norwegian -> Norwegian
                Languages.Dutch -> Dutch
                Languages.NorthernSotho -> NorthernSotho
                Languages.Polish -> Polish
                Languages.Pashto -> Pashto
                Languages.Portuguese -> Portuguese
                Languages.Quechua -> Quechua
                Languages.Romanian -> Romanian
                Languages.Russian -> Russian
                Languages.Slovak -> Slovak
                Languages.Albanian -> Albanian
                Languages.Swedish -> Swedish
                Languages.Swahili -> Swahili
                Languages.Tamil -> Tamil
                Languages.Telugu -> Telugu
                Languages.Tagalog -> Tagalog
                Languages.Tswana -> Tswana
                Languages.Turkish -> Turkish
                Languages.Tatar -> Tatar
                else -> Other(string)
            }
        }
    }
}
