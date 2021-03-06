export default {
    find (code) {
        if (!code) return ""
        const _language = this.language.find(e => e.key === code.toLowerCase() )
        return _language?_language.value:""
    },
    language:
    [
        // { key: 'aa', value: 'Afar' },
        // { key: 'ab', value: 'Abkhazian' },
        // { key: 'af', value: 'Afrikaans' },
        // { key: 'ak', value: 'Akan' },
        // { key: 'als', value: 'Alemannic' },
        // { key: 'am', value: 'Amharic' },
        // { key: 'an', value: 'Aragonese' },
        // { key: 'ang', value: 'Angal' },
        // { key: 'ang', value: 'Anglo-Saxon / Old English' },
        // { key: 'ar', value: 'Arabic' },
        // { key: 'arc', value: 'Aramaic' },
        // { key: 'as', value: 'Assamese' },
        // { key: 'ast', value: 'Asturian' },
        // { key: 'av', value: 'Avar' },
        // { key: 'awa', value: 'Awadhi' },
        // { key: 'ay', value: 'Aymara' },
        // { key: 'az', value: 'Azerbaijani' },
        // { key: 'ba', value: 'Bashkir' },
        // { key: 'bar', value: 'Bavarian' },
        // { key: 'bat-smg', value: 'Samogitian' },
        // { key: 'bcl', value: 'Bikol' },
         { key: 'be', value: 'Belarusian' },
        // { key: 'be-x-old', value: 'Belarusian (Taraškievica)' },
         { key: 'bg', value: 'Bulgarian' },
        // { key: 'bh', value: 'Bihari' },
        // { key: 'bi', value: 'Bislama' },
        // { key: 'bm', value: 'Bambara' },
        // { key: 'bn', value: 'Bengali' },
        // { key: 'bo', value: 'Tibetan' },
        // { key: 'bpy', value: 'Bishnupriya Manipuri' },
        // { key: 'br', value: 'Breton' },
        // { key: 'bs', value: 'Bosnian' },
        // { key: 'bug', value: 'Buginese' },
        // { key: 'bxr', value: 'Buriat (Russia)' },
        // { key: 'ca', value: 'Catalan' },
        // { key: 'cdo', value: 'Min Dong Chinese' },
        // { key: 'ce', value: 'Chechen' },
        // { key: 'ceb', value: 'Cebuano' },
        // { key: 'ch', value: 'Chamorro' },
        // { key: 'cho', value: 'Choctaw' },
        // { key: 'chr', value: 'Cherokee' },
        // { key: 'chy', value: 'Cheyenne' },
        // { key: 'co', value: 'Corsican' },
        // { key: 'cr', value: 'Cree' },
        // { key: 'cs', value: 'Czech' },
        // { key: 'csb', value: 'Kashubian' },
        // { key: 'cu', value: 'Old Church Slavonic / Old Bulgarian' },
        // { key: 'cv', value: 'Chuvash' },
        // { key: 'cy', value: 'Welsh' },
        // { key: 'da', value: 'Danish' },
         { key: 'de', value: 'German' },
        // { key: 'diq', value: 'Dimli' },
        // { key: 'dsb', value: 'Lower Sorbian' },
        // { key: 'dv', value: 'Divehi' },
        // { key: 'dz', value: 'Dzongkha' },
        // { key: 'ee', value: 'Ewe' },
        // { key: 'el', value: 'Greek' },
         { key: 'en', value: 'English' },
        // { key: 'eo', value: 'Esperanto' },
         { key: 'es', value: 'Spanish' },
         { key: 'et', value: 'Estonian' },
        // { key: 'eu', value: 'Basque' },
        // { key: 'ext', value: 'Extremaduran' },
        // { key: 'fa', value: 'Persian' },
        // { key: 'ff', value: 'Peul' },
        // { key: 'fi', value: 'Finnish' },
        // { key: 'fiu-vro', value: 'Võro' },
        // { key: 'fj', value: 'Fijian' },
        // { key: 'fo', value: 'Faroese' },
         { key: 'fr', value: 'French' },
        // { key: 'frp', value: 'Arpitan / Franco-Provençal' },
        // { key: 'fur', value: 'Friulian' },
        // { key: 'fy', value: 'West Frisian' },
        // { key: 'ga', value: 'Irish' },
        // { key: 'gan', value: 'Gan Chinese' },
        // { key: 'gbm', value: 'Garhwali' },
        // { key: 'gd', value: 'Scottish Gaelic' },
        // { key: 'gil', value: 'Gilbertese' },
        // { key: 'gl', value: 'Galician' },
        // { key: 'gn', value: 'Guarani' },
        // { key: 'got', value: 'Gothic' },
        // { key: 'gu', value: 'Gujarati' },
        // { key: 'gv', value: 'Manx' },
        // { key: 'ha', value: 'Hausa' },
        // { key: 'hak', value: 'Hakka Chinese' },
        // { key: 'haw', value: 'Hawaiian' },
        // { key: 'he', value: 'Hebrew' },
        // { key: 'hi', value: 'Hindi' },
        // { key: 'ho', value: 'Hiri Motu' },
        // { key: 'hr', value: 'Croatian' },
        // { key: 'ht', value: 'Haitian' },
        // { key: 'hu', value: 'Hungarian' },
        // { key: 'hy', value: 'Armenian' },
        // { key: 'hz', value: 'Herero' },
        // { key: 'ia', value: 'Interlingua' },
        // { key: 'id', value: 'Indonesian' },
        // { key: 'ie', value: 'Interlingue' },
        // { key: 'ig', value: 'Igbo' },
        // { key: 'ii', value: 'Sichuan Yi' },
        // { key: 'ik', value: 'Inupiak' },
        // { key: 'ilo', value: 'Ilokano' },
        // { key: 'inh', value: 'Ingush' },
        // { key: 'io', value: 'Ido' },
        // { key: 'is', value: 'Icelandic' },
         { key: 'it', value: 'Italian' },
        // { key: 'iu', value: 'Inuktitut' },
        // { key: 'ja', value: 'Japanese' },
        // { key: 'jbo', value: 'Lojban' },
        // { key: 'jv', value: 'Javanese' },
        // { key: 'ka', value: 'Georgian' },
        // { key: 'kg', value: 'Kongo' },
        // { key: 'ki', value: 'Kikuyu' },
        // { key: 'kj', value: 'Kuanyama' },
        // { key: 'kk', value: 'Kazakh' },
        // { key: 'kl', value: 'Greenlandic' },
        // { key: 'km', value: 'Cambodian' },
        // { key: 'kn', value: 'Kannada' },
        // { key: 'khw', value: 'Khowar' },
         { key: 'ko', value: 'Korean' },
        // { key: 'kr', value: 'Kanuri' },
        // { key: 'ks', value: 'Kashmiri' },
        // { key: 'ksh', value: 'Ripuarian' },
        // { key: 'ku', value: 'Kurdish' },
        // { key: 'kv', value: 'Komi' },
        // { key: 'kw', value: 'Cornish' },
        // { key: 'ky', value: 'Kirghiz' },
        // { key: 'la', value: 'Latin' },
        // { key: 'lad', value: 'Ladino / Judeo-Spanish' },
        // { key: 'lan', value: 'Lango' },
        // { key: 'lb', value: 'Luxembourgish' },
        // { key: 'lg', value: 'Ganda' },
        // { key: 'li', value: 'Limburgian' },
        // { key: 'lij', value: 'Ligurian' },
        // { key: 'lmo', value: 'Lombard' },
        // { key: 'ln', value: 'Lingala' },
        // { key: 'lo', value: 'Laotian' },
        // { key: 'lzz', value: 'Laz' },
        // { key: 'lt', value: 'Lithuanian' },
        // { key: 'lv', value: 'Latvian' },
        // { key: 'map-bms', value: 'Banyumasan' },
        // { key: 'mg', value: 'Malagasy' },
        // { key: 'man', value: 'Mandarin' },
        // { key: 'mh', value: 'Marshallese' },
        // { key: 'mi', value: 'Maori' },
        // { key: 'min', value: 'Minangkabau' },
        // { key: 'mk', value: 'Macedonian' },
        // { key: 'ml', value: 'Malayalam' },
        // { key: 'mn', value: 'Mongolian' },
        // { key: 'mo', value: 'Moldovan' },
        // { key: 'mr', value: 'Marathi' },
        // { key: 'ms', value: 'Malay' },
        // { key: 'mt', value: 'Maltese' },
        // { key: 'mus', value: 'Creek / Muskogee' },
        // { key: 'mwl', value: 'Mirandese' },
        // { key: 'my', value: 'Burmese' },
        // { key: 'na', value: 'Nauruan' },
        // { key: 'nah', value: 'Nahuatl' },
        // { key: 'nap', value: 'Neapolitan' },
        // { key: 'nd', value: 'North Ndebele' },
        // { key: 'nds', value: 'Low German / Low Saxon' },
        // { key: 'nds-nl', value: 'Dutch Low Saxon' },
        // { key: 'ne', value: 'Nepali' },
        // { key: 'new', value: 'Newar' },
        // { key: 'ng', value: 'Ndonga' },
        // { key: 'nl', value: 'Dutch' },
        // { key: 'nn', value: 'Norwegian Nynorsk' },
        // { key: 'no', value: 'Norwegian' },
        // { key: 'nr', value: 'South Ndebele' },
        // { key: 'nso', value: 'Northern Sotho' },
        // { key: 'nrm', value: 'Norman' },
        // { key: 'nv', value: 'Navajo' },
        // { key: 'ny', value: 'Chichewa' },
        // { key: 'oc', value: 'Occitan' },
        // { key: 'oj', value: 'Ojibwa' },
        // { key: 'om', value: 'Oromo' },
        // { key: 'or', value: 'Oriya' },
        // { key: 'os', value: 'Ossetian / Ossetic' },
        // { key: 'pa', value: 'Panjabi / Punjabi' },
        // { key: 'pag', value: 'Pangasinan' },
        // { key: 'pam', value: 'Kapampangan' },
        // { key: 'pap', value: 'Papiamentu' },
        // { key: 'pdc', value: 'Pennsylvania German' },
        // { key: 'pi', value: 'Pali' },
        // { key: 'pih', value: 'Norfolk' },
        // { key: 'pl', value: 'Polish' },
        // { key: 'pms', value: 'Piedmontese' },
        // { key: 'ps', value: 'Pashto' },
        // { key: 'pt', value: 'Portuguese' },
        // { key: 'qu', value: 'Quechua' },
        // { key: 'rm', value: 'Raeto Romance' },
        // { key: 'rmy', value: 'Romani' },
        // { key: 'rn', value: 'Kirundi' },
        // { key: 'ro', value: 'Romanian' },
        // { key: 'roa-rup', value: 'Aromanian' },
        // { key: 'ru', value: 'Russian' },
        // { key: 'rw', value: 'Rwandi' },
        // { key: 'sa', value: 'Sanskrit' },
        // { key: 'sc', value: 'Sardinian' },
        // { key: 'scn', value: 'Sicilian' },
        // { key: 'sco', value: 'Scots' },
        // { key: 'sd', value: 'Sindhi' },
        // { key: 'se', value: 'Northern Sami' },
        // { key: 'sg', value: 'Sango' },
        // { key: 'sh', value: 'Serbo-Croatian' },
        // { key: 'si', value: 'Sinhalese' },
        // { key: 'simple', value: 'Simple English' },
        // { key: 'sk', value: 'Slovak' },
        // { key: 'sl', value: 'Slovenian' },
        // { key: 'sm', value: 'Samoan' },
        // { key: 'sn', value: 'Shona' },
        // { key: 'so', value: 'Somalia' },
        // { key: 'sq', value: 'Albanian' },
        // { key: 'sr', value: 'Serbian' },
        // { key: 'ss', value: 'Swati' },
        // { key: 'st', value: 'Southern Sotho' },
        // { key: 'su', value: 'Sundanese' },
        // { key: 'sv', value: 'Swedish' },
        // { key: 'sw', value: 'Swahili' },
        // { key: 'ta', value: 'Tamil' },
        // { key: 'te', value: 'Telugu' },
        // { key: 'tet', value: 'Tetum' },
        // { key: 'tg', value: 'Tajik' },
        // { key: 'th', value: 'Thai' },
        // { key: 'ti', value: 'Tigrinya' },
        // { key: 'tk', value: 'Turkmen' },
        // { key: 'tl', value: 'Tagalog' },
        // { key: 'tlh', value: 'Klingon' },
        // { key: 'tn', value: 'Tswana' },
        // { key: 'to', value: 'Tonga' },
        // { key: 'tpi', value: 'Tok Pisin' },
        // { key: 'tr', value: 'Turkish' },
        // { key: 'ts', value: 'Tsonga' },
        // { key: 'tt', value: 'Tatar' },
        // { key: 'tum', value: 'Tumbuka' },
        // { key: 'tw', value: 'Twi' },
        // { key: 'ty', value: 'Tahitian' },
        // { key: 'udm', value: 'Udmurt' },
        // { key: 'ug', value: 'Uyghur' },
        // { key: 'uk', value: 'Ukrainian' },
        // { key: 'ur', value: 'Urdu' },
        // { key: 'uz', value: 'Uzbek' },
        // { key: 've', value: 'Venda' },
        // { key: 'vi', value: 'Vietnamese' },
        // { key: 'vec', value: 'Venetian' },
        // { key: 'vls', value: 'West Flemish' },
        // { key: 'vo', value: 'Volapük' },
        // { key: 'wa', value: 'Walloon' },
        // { key: 'war', value: 'Waray / Samar-Leyte Visayan' },
        // { key: 'wo', value: 'Wolof' },
        // { key: 'xal', value: 'Kalmyk' },
        // { key: 'xh', value: 'Xhosa' },
        // { key: 'xmf', value: 'Megrelian' },
        // { key: 'yi', value: 'Yiddish' },
        // { key: 'yo', value: 'Yoruba' },
        // { key: 'za', value: 'Zhuang' },
        // { key: 'zh', value: 'Chinese' },
        // { key: 'zh-classical', value: 'Classical Chinese' },
        // { key: 'zh-min-nan', value: 'Minnan' },
        // { key: 'zh-yue', value: 'Cantonese' },
        // { key: 'zu', value: 'Zulu' },
    ]
}