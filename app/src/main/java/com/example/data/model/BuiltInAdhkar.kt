package com.example.data.model

data class BuiltInDhikr(
    val id: String,
    val title: String,
    val titleEn: String,
    val content: String,
    val count: Int,
    val categoryId: String,
    val categoryNameAr: String,
    val categoryNameEn: String,
    val virtue: String = "", // الفضل
    val virtueEn: String = "",
    val source: String = "" // المصدر الموثق
)

data class DhikrCategory(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val iconEmoji: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val count: Int
)

object BuiltInAdhkarData {
    val categories: List<DhikrCategory> = listOf(
        DhikrCategory("morning", "أذكار الصباح", "Morning Adhkar", "🌅", "تحصين النفس وبدء اليوم بذكر الله", "Fortification and starting the day with Allah's remembrance", 8),
        DhikrCategory("evening", "أذكار المساء", "Evening Adhkar", "🌙", "حفظ وسكينة مع إقبال الليل", "Protection and peace as night approaches", 8),
        DhikrCategory("after_prayer", "أذكار بعد الصلاة", "After Prayer", "🕌", "الأذكار المشروعة دبر الصلوات المكتوبة", "Remembrances after the prescribed prayers", 6),
        DhikrCategory("sleep", "أذكار النوم", "Sleep Adhkar", "🛏️", "التحصين والأدعية قبل النوم", "Protection and supplications before sleep", 5),
        DhikrCategory("wake_up", "أذكار الاستيقاظ", "Waking Up", "☀️", "الحمد لله على نعمة الحياة والنشور", "Praising Allah upon waking", 3),
        DhikrCategory("food", "أذكار الطعام", "Food & Drink", "🍲", "التسمية والحمد والشكر للرازق", "Saying Bismillah and thanking the Provider", 3),
        DhikrCategory("travel", "أذكار السفر", "Travel Prayers", "🚗", "دعاء الركوب والسفر واستيداع الأهل", "Supplications for riding and journeying", 3),
        DhikrCategory("home", "دخول وخروج المنزل", "Entering & Leaving Home", "🚪", "التوكل على الله وحفظ البيت", "Relying on Allah when entering and exiting", 3),
        DhikrCategory("mosque", "أذكار المسجد", "Mosque Adhkar", "🕋", "دعاء الذهاب ودخول وخروج المسجد", "Prayers for going to, entering and leaving the mosque", 3),
        DhikrCategory("wudu", "أذكار الوضوء", "Ablution Adhkar", "💧", "الأذكار المشروعة عند الوضوء وفضله", "Supplications before and after ablution", 3),
        DhikrCategory("istighfar", "الاستغفار والتوبة", "Forgiveness & Repentance", "🤲", "سيد الاستغفار وأدعية التوبة النصوح", "Master of forgiveness and seeking repentance", 4),
        DhikrCategory("prophet_salawat", "الصلاة على النبي ﷺ", "Salawat on Prophet", "📿", "الصلاة الإبراهيمية وصيغ الصلاة الفاضلة", "Blessings upon the beloved Prophet Muhammad", 3),
        DhikrCategory("tasbeeh_praise", "التسبيح والتحميد", "Praise & Glorification", "💫", "أحب الكلام إلى الله وكنوز الجنة", "Dearest words to Allah and treasures of Paradise", 5),
        DhikrCategory("quranic_duas", "أدعية قرآنية ونبوية", "Quranic & Prophetic Duas", "📖", "جوامع الدعاء من الكتاب والسنة", "Comprehensive supplications from Quran and Sunnah", 6),
        DhikrCategory("custom", "أذكار مخصصة", "Custom Adhkar", "✨", "أذكارك وأدعيتك الخاصة المحفوظة محليًا", "Your personal custom supplications", 0)
    )

    val adhkarList: List<BuiltInDhikr> = listOf(
        // أذكار الصباح
        BuiltInDhikr(
            id = "m_1",
            title = "آية الكرسي",
            titleEn = "Ayat Al-Kursi",
            content = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            count = 1,
            categoryId = "morning",
            categoryNameAr = "أذكار الصباح",
            categoryNameEn = "Morning Adhkar",
            virtue = "من قرأها حين يصبح أجير من الجن حتى يمسي",
            virtueEn = "Whoever recites it in the morning is protected until evening",
            source = "رواه الحاكم وصححه الألباني"
        ),
        BuiltInDhikr(
            id = "m_2",
            title = "أصبحنا وأصبح الملك لله",
            titleEn = "Asbahna wa Asbahal Mulk",
            content = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَٰذَا الْيَوْمِ وَخَيْرَ مَا بَعْدَهُ، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَٰذَا الْيَوْمِ وَشَرِّ مَا بَعْدَهُ، رَبِّ أَعُوذُ بِكَ مِنَ الْكَسَلِ وَسُوءِ الْكِبَرِ، رَبِّ أَعُوذُ بِكَ مِنْ عَذَابٍ فِي النَّارِ وَعَذَابٍ فِي الْقَبْرِ",
            count = 1,
            categoryId = "morning",
            categoryNameAr = "أذكار الصباح",
            categoryNameEn = "Morning Adhkar",
            virtue = "سؤال الله خير اليوم والتعوذ من الشرور والكسل وعذاب القبر",
            virtueEn = "Asking Allah for the good of the day and seeking refuge from evil",
            source = "صحيح مسلم (2723)"
        ),
        BuiltInDhikr(
            id = "m_3",
            title = "سيد الاستغفار",
            titleEn = "Sayyid Al-Istighfar",
            content = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            count = 1,
            categoryId = "morning",
            categoryNameAr = "أذكار الصباح",
            categoryNameEn = "Morning Adhkar",
            virtue = "من قالها موقنًا بها حين يصبح فمات من يومه دخل الجنة",
            virtueEn = "Whoever recites it with conviction and dies that day enters Paradise",
            source = "صحيح البخاري (6306)"
        ),
        BuiltInDhikr(
            id = "m_4",
            title = "اللهم بك أصبحنا",
            titleEn = "Allahumma bika asbahna",
            content = "اللَّهُمَّ بِكَ أَصْبَحْنَا، وَبِكَ أَمْسَيْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ النُّشُورُ",
            count = 1,
            categoryId = "morning",
            categoryNameAr = "أذكار الصباح",
            categoryNameEn = "Morning Adhkar",
            virtue = "اعتراف بالافتقار إلى الله في الحياة والموت",
            source = "سنن الترمذي (3391)"
        ),
        BuiltInDhikr(
            id = "m_5",
            title = "رضيت بالله رباً",
            titleEn = "Raditu Billahi Rabba",
            content = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا",
            count = 3,
            categoryId = "morning",
            categoryNameAr = "أذكار الصباح",
            categoryNameEn = "Morning Adhkar",
            virtue = "كان حقاً على الله أن يرضيه يوم القيامة",
            source = "مسند أحمد وسنن أبي داود (5072)"
        ),
        BuiltInDhikr(
            id = "m_6",
            title = "بسم الله الذي لا يضر مع اسمه شيء",
            titleEn = "Bismillahilladhi la yadurru",
            content = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
            count = 3,
            categoryId = "morning",
            categoryNameAr = "أذكار الصباح",
            categoryNameEn = "Morning Adhkar",
            virtue = "لم يضره شيء في ذلك اليوم حتى يمسي",
            source = "سنن الترمذي (3388) وأبو داود"
        ),
        BuiltInDhikr(
            id = "m_7",
            title = "يا حي يا قيوم برحمتك أستغيث",
            titleEn = "Ya Hayyu Ya Qayyum",
            content = "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ، وَلَا تَكِلْنِي إِلَىٰ نَفْسِي طَرْفَةَ عَيْنٍ",
            count = 1,
            categoryId = "morning",
            categoryNameAr = "أذكار الصباح",
            categoryNameEn = "Morning Adhkar",
            virtue = "تفويض الأمر إلى الله وإصلاح الشأن كله",
            source = "سنن النسائي الكبرى وحسنه الألباني"
        ),
        BuiltInDhikr(
            id = "m_8",
            title = "المعوذات والإخلاص",
            titleEn = "Al-Mu'awwidhat & Al-Ikhlas",
            content = "قُلْ هُوَ اللَّهُ أَحَدٌ... قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ... قُلْ أَعُوذُ بِرَبِّ النَّاسِ...",
            count = 3,
            categoryId = "morning",
            categoryNameAr = "أذكار الصباح",
            categoryNameEn = "Morning Adhkar",
            virtue = "تكفيك من كل شيء",
            source = "سنن أبي داود والترمذي"
        ),

        // أذكار المساء
        BuiltInDhikr(
            id = "e_1",
            title = "أمسينا وأمسى الملك لله",
            titleEn = "Amsayna wa Amsal Mulk",
            content = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَٰذِهِ اللَّيْلَةِ وَخَيْرَ مَا بَعْدَهَا، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَٰذِهِ اللَّيْلَةِ وَشَرِّ مَا بَعْدَهَا، رَبِّ أَعُوذُ بِكَ مِنَ الْكَسَلِ وَسُوءِ الْكِبَرِ، رَبِّ أَعُوذُ بِكَ مِنْ عَذَابٍ فِي النَّارِ وَعَذَابٍ فِي الْقَبْرِ",
            count = 1,
            categoryId = "evening",
            categoryNameAr = "أذكار المساء",
            categoryNameEn = "Evening Adhkar",
            virtue = "حفظ الليلة والاستعاذة من كل سوء وشر",
            source = "صحيح مسلم (2723)"
        ),
        BuiltInDhikr(
            id = "e_2",
            title = "أعوذ بكلمات الله التامات",
            titleEn = "A'udhu bi kalimatillah",
            content = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
            count = 3,
            categoryId = "evening",
            categoryNameAr = "أذكار المساء",
            categoryNameEn = "Evening Adhkar",
            virtue = "لم يضره شيء حتى يصبح وتأمينه من لدغ الهوام",
            source = "صحيح مسلم (2709)"
        ),
        BuiltInDhikr(
            id = "e_3",
            title = "اللهم بك أمسينا",
            titleEn = "Allahumma bika amsayna",
            content = "اللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ الْمَصِيرُ",
            count = 1,
            categoryId = "evening",
            categoryNameAr = "أذكار المساء",
            categoryNameEn = "Evening Adhkar",
            virtue = "التسليم لله والاعتراف بالرجوع إليه",
            source = "سنن الترمذي (3391)"
        ),
        BuiltInDhikr(
            id = "e_4",
            title = "حسبي الله لا إله إلا هو",
            titleEn = "Hasbiyallahu la ilaha illa Huwa",
            content = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ ۖ عَلَيْهِ تَوَكَّلْتُ ۖ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
            count = 7,
            categoryId = "evening",
            categoryNameAr = "أذكار المساء",
            categoryNameEn = "Evening Adhkar",
            virtue = "كفاه الله ما أهمه من أمر الدنيا والآخرة",
            source = "سنن أبي داود (5081)"
        ),
        BuiltInDhikr(
            id = "e_5",
            title = "اللهم عافني في بدني",
            titleEn = "Allahumma 'afini fi badani",
            content = "اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لَا إِلَٰهَ إِلَّا أَنْتَ. اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْكُفْرِ وَالْفَقْرِ، وَأَعُوذُ بِكَ مِنْ عَذَابِ الْقَبْرِ، لَا إِلَٰهَ إِلَّا أَنْتَ",
            count = 3,
            categoryId = "evening",
            categoryNameAr = "أذكار المساء",
            categoryNameEn = "Evening Adhkar",
            virtue = "دعاء العافية في الجسد والسمع والبصر",
            source = "سنن أبي داود (5090)"
        ),
        BuiltInDhikr(
            id = "e_6",
            title = "اللهم ما أمسى بي من نعمة",
            titleEn = "Allahumma ma amsa bi min ni'mah",
            content = "اللَّهُمَّ مَا أَمْسَى بِي مِنْ نِعْمَةٍ أَوْ بِأَحَدٍ مِنْ خَلْقِكَ، فَمِنْكَ وَحْدَكَ لَا شَرِيكَ لَكَ، فَلَكَ الْحَمْدُ وَلَكَ الشُّكْرُ",
            count = 1,
            categoryId = "evening",
            categoryNameAr = "أذكار المساء",
            categoryNameEn = "Evening Adhkar",
            virtue = "من قالها حين يمسي فقد أدى شكر ليلته",
            source = "سنن أبي داود (5073)"
        ),
        BuiltInDhikr(
            id = "e_7",
            title = "سبحان الله وبحمده (مئة مرة)",
            titleEn = "Subhanallahi wa bihamdihi (100x)",
            content = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            count = 100,
            categoryId = "evening",
            categoryNameAr = "أذكار المساء",
            categoryNameEn = "Evening Adhkar",
            virtue = "حُطّت خطاياه وإن كانت مثل زبد البحر ولم يأت أحد بأفضل مما جاء به",
            source = "صحيح مسلم (2692)"
        ),
        BuiltInDhikr(
            id = "e_8",
            title = "خواتيم سورة البقرة",
            titleEn = "Last verses of Al-Baqarah",
            content = "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِّن رُّسُلِهِ ۚ وَقَالُوا سَمِعْنَا وَأَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ...",
            count = 1,
            categoryId = "evening",
            categoryNameAr = "أذكار المساء",
            categoryNameEn = "Evening Adhkar",
            virtue = "من قرأهما في ليلة كفتاه",
            source = "صحيح البخاري (5009)"
        ),

        // أذكار بعد الصلاة
        BuiltInDhikr(
            id = "ap_1",
            title = "الاستغفار بعد السلام",
            titleEn = "Astaghfirullah after Salam",
            content = "أَسْتَغْفِرُ اللَّهَ، أَسْتَغْفِرُ اللَّهَ، أَسْتَغْفِرُ اللَّهَ. اللَّهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ السَّلَامُ، تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ",
            count = 1,
            categoryId = "after_prayer",
            categoryNameAr = "أذكار بعد الصلاة",
            categoryNameEn = "After Prayer",
            virtue = "السنة الثابتة عن النبي ﷺ فور التسليم من الفريضة",
            source = "صحيح مسلم (591)"
        ),
        BuiltInDhikr(
            id = "ap_2",
            title = "التهليل لا إله إلا الله وحده",
            titleEn = "Tahlil after Prayer",
            content = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ، اللَّهُمَّ لَا مَانِعَ لِمَا أَعْطَيْتَ، وَلَا مُعْطِيَ لِمَا مَنَعْتَ، وَلَا يَنْفَعُ ذَا الْجَدِّ مِنْكَ الْجَدُّ",
            count = 1,
            categoryId = "after_prayer",
            categoryNameAr = "أذكار بعد الصلاة",
            categoryNameEn = "After Prayer",
            virtue = "توحيد خالص وتعظيم لقضاء الله وقدره",
            source = "صحيح البخاري (844) ومسلم (593)"
        ),
        BuiltInDhikr(
            id = "ap_3",
            title = "التسبيح والتحميد والتكبير (33x)",
            titleEn = "Tasbeeh, Tahmeed, Takbeer 33x",
            content = "سُبْحَانَ اللَّهِ (33) ، الْحَمْدُ لِلَّهِ (33) ، اللَّهُ أَكْبَرُ (33) ، ثُمَّ تَمَامَ الْمِائَةِ: لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            count = 33,
            categoryId = "after_prayer",
            categoryNameAr = "أذكار بعد الصلاة",
            categoryNameEn = "After Prayer",
            virtue = "غُفرت خطاياه وإن كانت مثل زبد البحر",
            source = "صحيح مسلم (597)"
        ),
        BuiltInDhikr(
            id = "ap_4",
            title = "آية الكرسي دبر كل صلاة",
            titleEn = "Ayat Al-Kursi after each Prayer",
            content = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ... وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            count = 1,
            categoryId = "after_prayer",
            categoryNameAr = "أذكار بعد الصلاة",
            categoryNameEn = "After Prayer",
            virtue = "من قرأها دبر كل صلاة مكتوبة لم يمنعه من دخول الجنة إلا أن يموت",
            source = "رواه النسائي في عمل اليوم والليلة وصححه الألباني"
        ),
        BuiltInDhikr(
            id = "ap_5",
            title = "المعوذات بعد الصلاة",
            titleEn = "Surahs Al-Ikhlas, Al-Falaq, An-Nas",
            content = "قراءة: سورة الإخلاص، وسورة الفلق، وسورة الناس (مرة بعد كل صلاة، وثلاثاً بعد الفجر والمغرب)",
            count = 1,
            categoryId = "after_prayer",
            categoryNameAr = "أذكار بعد الصلاة",
            categoryNameEn = "After Prayer",
            virtue = "حفظ وتعويذ نبوي",
            source = "سنن أبي داود (1523)"
        ),
        BuiltInDhikr(
            id = "ap_6",
            title = "اللهم أعني على ذكرك وشكرك",
            titleEn = "Allahumma a'inni 'ala dhikrika",
            content = "اللَّهُمَّ أَعِنِّي عَلَىٰ ذِكْرِكَ، وَشُكْرِكَ، وَحُسْنِ عِبَادَتِكَ",
            count = 1,
            categoryId = "after_prayer",
            categoryNameAr = "أذكار بعد الصلاة",
            categoryNameEn = "After Prayer",
            virtue = "وصية النبي ﷺ لمعاذ بن جبل ألا يدعها دبر كل صلاة",
            source = "سنن أبي داود (1522) وصححه الألباني"
        ),

        // أذكار النوم
        BuiltInDhikr(
            id = "sl_1",
            title = "باسمك ربي وضعت جنبي",
            titleEn = "Bismika Rabbi wada'tu janbi",
            content = "بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي، وَبِكَ أَرْفَعُهُ، فَإِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ",
            count = 1,
            categoryId = "sleep",
            categoryNameAr = "أذكار النوم",
            categoryNameEn = "Sleep Adhkar",
            virtue = "حفظ الروح والجسد في النوم واليقظة",
            source = "صحيح البخاري (6320) ومسلم (2714)"
        ),
        BuiltInDhikr(
            id = "sl_2",
            title = "اللهم قني عذابك يوم تبعث عبادك",
            titleEn = "Allahumma qini 'adhabaka",
            content = "اللَّهُمَّ قِنِي عَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ",
            count = 3,
            categoryId = "sleep",
            categoryNameAr = "أذكار النوم",
            categoryNameEn = "Sleep Adhkar",
            virtue = "كان النبي ﷺ إذا أراد أن يرقد وضع يده اليمنى تحت خده ثم قالها ثلاثاً",
            source = "سنن أبي داود والترمذي"
        ),
        BuiltInDhikr(
            id = "sl_3",
            title = "باسمك اللهم أموت وأحيا",
            titleEn = "Bismikallahumma amutu wa ahya",
            content = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
            count = 1,
            categoryId = "sleep",
            categoryNameAr = "أذكار النوم",
            categoryNameEn = "Sleep Adhkar",
            virtue = "التسليم لله عند الموت الأصغر",
            source = "صحيح البخاري (6324)"
        ),
        BuiltInDhikr(
            id = "sl_4",
            title = "تسبيح النوم (33x سبحان الله، 33x الحمد لله، 34x الله أكبر)",
            titleEn = "Sleep Tasbeeh (33, 33, 34)",
            content = "سُبْحَانَ اللَّهِ (33) ، وَالْحَمْدُ لِلَّهِ (33) ، وَاللَّهُ أَكْبَرُ (34)",
            count = 34,
            categoryId = "sleep",
            categoryNameAr = "أذكار النوم",
            categoryNameEn = "Sleep Adhkar",
            virtue = "وصية النبي ﷺ لعلي وفاطمة رضي الله عنهما: خير لكما من خادم",
            source = "صحيح البخاري (3705) ومسلم (2727)"
        ),
        BuiltInDhikr(
            id = "sl_5",
            title = "دعاء النوم الجامع (اللهم أسلمت نفسي إليك)",
            titleEn = "Allahumma aslamtu nafsi ilayk",
            content = "اللَّهُمَّ أَسْلَمْتُ نَفْسِي إِلَيْكَ، وَفَوَّضْتُ أَمْرِي إِلَيْكَ، وَوَجَّهْتُ وَجْهِي إِلَيْكَ، وَأَلْجَأْتُ ظَهْرِي إِلَيْكَ، رَغْبَةً وَرَهْبَةً إِلَيْكَ، لَا مَلْجَأَ وَلَا مَنْجَا مِنْكَ إِلَّا إِلَيْكَ، آمَنْتُ بِكِتَابِكَ الَّذِي أَنْزَلْتَ، وَبِنَبِيِّكَ الَّذِي أَرْسَلْتَ",
            count = 1,
            categoryId = "sleep",
            categoryNameAr = "أذكار النوم",
            categoryNameEn = "Sleep Adhkar",
            virtue = "إذا مت من ليلتك مت على الفطرة واجعلهن آخر ما تتكلم به",
            source = "صحيح البخاري (247) ومسلم (2710)"
        ),

        // أذكار الاستيقاظ
        BuiltInDhikr(
            id = "w_1",
            title = "الحمد لله الذي أحيانا",
            titleEn = "Alhamdulillahil-ladhi ahyana",
            content = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
            count = 1,
            categoryId = "wake_up",
            categoryNameAr = "أذكار الاستيقاظ",
            categoryNameEn = "Waking Up",
            virtue = "شكر الله على استعادة الروح والنشاط",
            source = "صحيح البخاري (6312)"
        ),
        BuiltInDhikr(
            id = "w_2",
            title = "الحمد لله الذي عافاني في جسدي",
            titleEn = "Alhamdulillahil-ladhi 'afani fi jasadi",
            content = "الْحَمْدُ لِلَّهِ الَّذِي عَافَانِي فِي جَسَدِي، وَرَدَّ عَلَيَّ رُوحِي، وَأَذِنَ لِي بِذِكْرِهِ",
            count = 1,
            categoryId = "wake_up",
            categoryNameAr = "أذكار الاستيقاظ",
            categoryNameEn = "Waking Up",
            virtue = "شكر عافية الجسد والإنعام بالذكر",
            source = "سنن الترمذي (3401) وحسنه الألباني"
        ),
        BuiltInDhikr(
            id = "w_3",
            title = "دعاء التعارّ من الليل",
            titleEn = "Dua upon waking at night",
            content = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ، سُبْحَانَ اللَّهِ، وَالْحَمْدُ لِلَّهِ، وَلَا إِلَٰهَ إِلَّا اللَّهُ، وَاللَّهُ أَكْبَرُ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ، اللَّهُمَّ اغْفِرْ لِي",
            count = 1,
            categoryId = "wake_up",
            categoryNameAr = "أذكار الاستيقاظ",
            categoryNameEn = "Waking Up",
            virtue = "من قال ذلك ثم دعا استجيب له، فإن توضأ وصلى قبلت صلاته",
            source = "صحيح البخاري (1154)"
        ),

        // أذكار الطعام
        BuiltInDhikr(
            id = "fd_1",
            title = "التسمية قبل الأكل",
            titleEn = "Bismillah before eating",
            content = "بِسْمِ اللَّهِ (وإن نسي في أوله: بِسْمِ اللَّهِ فِي أَوَّلِهِ وَآخِرِهِ)",
            count = 1,
            categoryId = "food",
            categoryNameAr = "أذكار الطعام",
            categoryNameEn = "Food & Drink",
            virtue = "البركة في الطعام ومنع الشيطان من مشاركته",
            source = "سنن أبي داود (3767) والترمذي"
        ),
        BuiltInDhikr(
            id = "fd_2",
            title = "الحمد بعد الفراغ من الطعام",
            titleEn = "Alhamdulillah after eating",
            content = "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنِي هَٰذَا الطَّعَامَ وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍ",
            count = 1,
            categoryId = "food",
            categoryNameAr = "أذكار الطعام",
            categoryNameEn = "Food & Drink",
            virtue = "غُفر له ما تقدم من ذنبه",
            source = "سنن الترمذي (3458) وأبو داود"
        ),
        BuiltInDhikr(
            id = "fd_3",
            title = "دعاء الضيف لأهل الطعام",
            titleEn = "Guest Dua for the host",
            content = "اللَّهُمَّ بَارِكْ لَهُمْ فِيمَا رَزَقْتَهُمْ، وَاغْفِرْ لَهُمْ، وَارْحَمْهُمْ",
            count = 1,
            categoryId = "food",
            categoryNameAr = "أذكار الطعام",
            categoryNameEn = "Food & Drink",
            virtue = "دعاء مأثور بالبركة والمغفرة لمن أطعمك",
            source = "صحيح مسلم (2042)"
        ),

        // أذكار السفر
        BuiltInDhikr(
            id = "tr_1",
            title = "دعاء ركوب الدابة والمركبة",
            titleEn = "Dua for riding vehicle",
            content = "بِسْمِ اللَّهِ، الْحَمْدُ لِلَّهِ، ﴿سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ * وَإِنَّا إِلَىٰ رَبِّنَا لَمُنقَلِبُونَ﴾، الْحَمْدُ لِلَّهِ، الْحَمْدُ لِلَّهِ، الْحَمْدُ لِلَّهِ، اللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ، سُبْحَانَكَ اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            count = 1,
            categoryId = "travel",
            categoryNameAr = "أذكار السفر",
            categoryNameEn = "Travel Prayers",
            virtue = "تيسير الرحلة وشكر نعمة الركوب والمواصلات",
            source = "سنن أبي داود (2602) والترمذي (3446)"
        ),
        BuiltInDhikr(
            id = "tr_2",
            title = "دعاء السفر الكامل",
            titleEn = "Full Travel Supplication",
            content = "اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَٰذَا الْبِرَّ وَالتَّقْوَىٰ، وَمِنَ الْعَمَلِ مَا تَرْضَىٰ، اللَّهُمَّ هَوِّنْ عَلَيْنَا سَفَرَنَا هَٰذَا وَاطْوِ عَنَّا بُعْدَهُ، اللَّهُمَّ أَنْتَ الصَّاحِبُ فِي السَّفَرِ، وَالْخَلِيفَةُ فِي الْأَهْلِ، اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ وَعْثَاءِ السَّفَرِ، وَكَآبَةِ الْمَنْظَرِ، وَسُوءِ الْمُنْقَلَبِ فِي الْمَالِ وَالْأَهْلِ",
            count = 1,
            categoryId = "travel",
            categoryNameAr = "أذكار السفر",
            categoryNameEn = "Travel Prayers",
            virtue = "طلب الصحبة المباركة والسلامة في السفر والعودة",
            source = "صحيح مسلم (1342)"
        ),
        BuiltInDhikr(
            id = "tr_3",
            title = "دعاء الرجوع من السفر",
            titleEn = "Dua upon returning from travel",
            content = "آيِبُونَ تَائِبُونَ عَابِدُونَ لِرَبِّنَا حَامِدُونَ",
            count = 3,
            categoryId = "travel",
            categoryNameAr = "أذكار السفر",
            categoryNameEn = "Travel Prayers",
            virtue = "شكر العودة سالماً وتجديد التوبة والحمد",
            source = "صحيح البخاري ومسلم"
        ),

        // دخول وخروج المنزل
        BuiltInDhikr(
            id = "hm_1",
            title = "دعاء الخروج من المنزل",
            titleEn = "Dua when leaving home",
            content = "بِسْمِ اللَّهِ، تَوَكَّلْتُ عَلَىٰ اللَّهِ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            count = 1,
            categoryId = "home",
            categoryNameAr = "دخول وخروج المنزل",
            categoryNameEn = "Entering & Leaving Home",
            virtue = "يقال له: كفيت وهديت ووقيت وتنحى عنه الشيطان",
            source = "سنن أبي داود (5095) والترمذي"
        ),
        BuiltInDhikr(
            id = "hm_2",
            title = "الاستعاذة عند الخروج",
            titleEn = "Seeking refuge when leaving home",
            content = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ أَنْ أَضِلَّ أَوْ أُضَلَّ، أَوْ أَزِلَّ أَوْ أُزَلَّ، أَوْ أَظْلِمَ أَوْ أُظْلَمَ، أَوْ أَجْهَلَ أَوْ يُجْهَلَ عَلَيَّ",
            count = 1,
            categoryId = "home",
            categoryNameAr = "دخول وخروج المنزل",
            categoryNameEn = "Entering & Leaving Home",
            virtue = "دعاء نبوي شريف عند الخروج للمعاملات",
            source = "سنن أبي داود (5094)"
        ),
        BuiltInDhikr(
            id = "hm_3",
            title = "دعاء دخول المنزل",
            titleEn = "Dua when entering home",
            content = "بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا، وَعَلَىٰ اللَّهِ رَبِّنَا تَوَكَّلْنَا (ثم ليسلِّم على أهله)",
            count = 1,
            categoryId = "home",
            categoryNameAr = "دخول وخروج المنزل",
            categoryNameEn = "Entering & Leaving Home",
            virtue = "يقول الشيطان: لا مبيت لكم ولا عشاء في هذا البيت",
            source = "سنن أبي داود (5096) وصحيح مسلم"
        ),

        // أذكار المسجد
        BuiltInDhikr(
            id = "mq_1",
            title = "دعاء الذهاب إلى المسجد",
            titleEn = "Dua on the way to the mosque",
            content = "اللَّهُمَّ اجْعَلْ فِي قَلْبِي نُورًا، وَفِي لِسَانِي نُورًا، وَفِي سَمْعِي نُورًا، وَفِي بَصَرِي نُورًا، وَمِنْ فَوْقِي نُورًا، وَمِنْ تَحْتِي نُورًا، وَعَنْ يَمِينِي نُورًا، وَعَنْ شِمَالِي نُورًا، وَمِنْ أَمَامِي نُورًا، وَمِنْ خَلْفِي نُورًا، وَاجْعَلْ فِي نَفْسِي نُورًا، وَأَعْظِمْ لِي نُورًا",
            count = 1,
            categoryId = "mosque",
            categoryNameAr = "أذكار المسجد",
            categoryNameEn = "Mosque Adhkar",
            virtue = "سؤال النور الإلهي الشامل من كل جهة",
            source = "صحيح مسلم (763)"
        ),
        BuiltInDhikr(
            id = "mq_2",
            title = "دعاء دخول المسجد",
            titleEn = "Dua when entering mosque",
            content = "أَعُوذُ بِاللَّهِ الْعَظِيمِ، وَبِوَجْهِهِ الْكَرِيمِ، وَسُلْطَانِهِ الْقَدِيمِ، مِنَ الشَّيْطَانِ الرَّجِيمِ. بِسْمِ اللَّهِ، وَالصَّلَاةُ وَالسَّلَامُ عَلَىٰ رَسُولِ اللَّهِ، اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ",
            count = 1,
            categoryId = "mosque",
            categoryNameAr = "أذكار المسجد",
            categoryNameEn = "Mosque Adhkar",
            virtue = "فتح أبواب رحمة الله وحفظه من الشيطان سائر اليوم",
            source = "سنن أبي داود (465) وصحيح مسلم"
        ),
        BuiltInDhikr(
            id = "mq_3",
            title = "دعاء الخروج من المسجد",
            titleEn = "Dua when leaving mosque",
            content = "بِسْمِ اللَّهِ، وَالصَّلَاةُ وَالسَّلَامُ عَلَىٰ رَسُولِ اللَّهِ، اللَّهُمَّ إِنِّي أَسْأَلُكَ مِنْ فَضْلِكَ، اللَّهُمَّ اعْصِمْنِي مِنَ الشَّيْطَانِ الرَّجِيمِ",
            count = 1,
            categoryId = "mosque",
            categoryNameAr = "أذكار المسجد",
            categoryNameEn = "Mosque Adhkar",
            virtue = "طلب فضل الله ورزقه الحلال والعصمة من الشيطان",
            source = "صحيح مسلم (713) وسنن ابن ماجه"
        ),

        // أذكار الوضوء
        BuiltInDhikr(
            id = "wd_1",
            title = "التسمية قبل الوضوء",
            titleEn = "Bismillah before Wudu",
            content = "بِسْمِ اللَّهِ",
            count = 1,
            categoryId = "wudu",
            categoryNameAr = "أذكار الوضوء",
            categoryNameEn = "Ablution Adhkar",
            virtue = "لا وضوء لمن لم يذكر اسم الله عليه",
            source = "سنن أبي داود والترمذي"
        ),
        BuiltInDhikr(
            id = "wd_2",
            title = "الشهادة بعد الفراغ من الوضوء",
            titleEn = "Shahadah after Wudu",
            content = "أَشْهَدُ أَنْ لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
            count = 1,
            categoryId = "wudu",
            categoryNameAr = "أذكار الوضوء",
            categoryNameEn = "Ablution Adhkar",
            virtue = "فُتحت له أبواب الجنة الثمانية يدخل من أيها شاء",
            source = "صحيح مسلم (234)"
        ),
        BuiltInDhikr(
            id = "wd_3",
            title = "دعاء الطهارة والتوبة بعد الوضوء",
            titleEn = "Dua of purity after Wudu",
            content = "اللَّهُمَّ اجْعَلْنِي مِنَ التَّوَّابِينَ، وَاجْعَلْنِي مِنَ الْمُتَطَهِّرِينَ",
            count = 1,
            categoryId = "wudu",
            categoryNameAr = "أذكار الوضوء",
            categoryNameEn = "Ablution Adhkar",
            virtue = "الجمع بين طهارة الظاهر بالوضوء وطهارة الباطن بالتوبة",
            source = "سنن الترمذي (55) وصححه الألباني"
        ),

        // الاستغفار والتوبة
        BuiltInDhikr(
            id = "is_1",
            title = "أستغفر الله العظيم وأتوب إليه",
            titleEn = "Astaghfirullahal 'Adheem",
            content = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ الَّذِي لَا إِلَٰهَ إِلَّا هُوَ الْحَيَّ الْقَيُّومَ وَأَتُوبُ إِلَيْهِ",
            count = 100,
            categoryId = "istighfar",
            categoryNameAr = "الاستغفار والتوبة",
            categoryNameEn = "Forgiveness & Repentance",
            virtue = "غفرت ذنوبه وإن كان فر من الزحف",
            source = "سنن أبي داود والترمذي"
        ),
        BuiltInDhikr(
            id = "is_2",
            title = "رب اغفر لي وتب علي",
            titleEn = "Rabbighfir li wa tub 'alayya",
            content = "رَبِّ اغْفِرْ لِي وَتُبْ عَلَيَّ، إِنَّكَ أَنْتَ التَّوَّابُ الرَّحِيمُ",
            count = 100,
            categoryId = "istighfar",
            categoryNameAr = "الاستغفار والتوبة",
            categoryNameEn = "Forgiveness & Repentance",
            virtue = "كان يُعد لرسول الله ﷺ في المجلس الواحد مائة مرة",
            source = "سنن أبي داود (1516) والترمذي"
        ),
        BuiltInDhikr(
            id = "is_3",
            title = "دعاء يونس عليه السلام في بطن الحوت",
            titleEn = "Supplication of Prophet Yunus",
            content = "لَا إِلَٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
            count = 33,
            categoryId = "istighfar",
            categoryNameAr = "الاستغفار والتوبة",
            categoryNameEn = "Forgiveness & Repentance",
            virtue = "لم يدع بها رجل مسلم في شيء قط إلا استجاب الله له",
            source = "سنن الترمذي (3505) وصححه الألباني"
        ),
        BuiltInDhikr(
            id = "is_4",
            title = "اللهم إني ظلمت نفسي ظلماً كثيراً",
            titleEn = "Allahumma inni zalamtu nafsi",
            content = "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا، وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ، وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ",
            count = 1,
            categoryId = "istighfar",
            categoryNameAr = "الاستغفار والتوبة",
            categoryNameEn = "Forgiveness & Repentance",
            virtue = "علّمها النبي ﷺ لأبي بكر الصديق ليدعو بها في صلاته",
            source = "صحيح البخاري (834) ومسلم (2705)"
        ),

        // الصلاة على النبي ﷺ
        BuiltInDhikr(
            id = "sn_1",
            title = "الصلاة الإبراهيمية",
            titleEn = "As-Salat Al-Ibrahimiya",
            content = "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ، كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ، كَمَا بَارَكْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ",
            count = 10,
            categoryId = "prophet_salawat",
            categoryNameAr = "الصلاة على النبي ﷺ",
            categoryNameEn = "Salawat on Prophet",
            virtue = "أفضل صيغ الصلاة على النبي ﷺ وهي الواردة في التشهد الأخير",
            source = "صحيح البخاري (3370) ومسلم (405)"
        ),
        BuiltInDhikr(
            id = "sn_2",
            title = "الصيغة المختصرة للصلاة على النبي",
            titleEn = "Short Salawat",
            content = "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَىٰ نَبِيِّنَا مُحَمَّدٍ",
            count = 10,
            categoryId = "prophet_salawat",
            categoryNameAr = "الصلاة على النبي ﷺ",
            categoryNameEn = "Salawat on Prophet",
            virtue = "من صلى علي حين يصبح عشراً وحين يمسي عشراً أدركته شفاعتي يوم القيامة",
            source = "رواه الطبراني وحسنه الألباني"
        ),
        BuiltInDhikr(
            id = "sn_3",
            title = "صلاة الفرج وتفريج الهموم",
            titleEn = "Relief through Salawat",
            content = "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ عَبْدِكَ وَرَسُولِكَ النَّبِيِّ الأُمِّيِّ وَعَلَىٰ آلِهِ وَصَحْبِهِ وَسَلِّمْ تَسْلِيمًا",
            count = 100,
            categoryId = "prophet_salawat",
            categoryNameAr = "الصلاة على النبي ﷺ",
            categoryNameEn = "Salawat on Prophet",
            virtue = "إذاً تُكفى همَّك ويُغفر لك ذنبُك",
            source = "سنن الترمذي (2457)"
        ),

        // التسبيح والتحميد
        BuiltInDhikr(
            id = "tb_1",
            title = "كلمتان خفيفتان على اللسان",
            titleEn = "Two words light on tongue",
            content = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ",
            count = 100,
            categoryId = "tasbeeh_praise",
            categoryNameAr = "التسبيح والتحميد",
            categoryNameEn = "Praise & Glorification",
            virtue = "خفيفتان على اللسان، ثقيلتان في الميزان، حبيبتان إلى الرحمن",
            source = "صحيح البخاري (6406) ومسلم (2694)"
        ),
        BuiltInDhikr(
            id = "tb_2",
            title = "أحب الكلام إلى الله الأربع",
            titleEn = "The Four beloved words to Allah",
            content = "سُبْحَانَ اللَّهِ، وَالْحَمْدُ لِلَّهِ، وَلَا إِلَٰهَ إِلَّا اللَّهُ، وَاللَّهُ أَكْبَرُ",
            count = 100,
            categoryId = "tasbeeh_praise",
            categoryNameAr = "التسبيح والتحميد",
            categoryNameEn = "Praise & Glorification",
            virtue = "أحب الكلام إلى الله، لا يضرك بأيهن بدأت، وهن الباقيات الصالحات",
            source = "صحيح مسلم (2137)"
        ),
        BuiltInDhikr(
            id = "tb_3",
            title = "كنز من كنوز الجنة (الحوقلة)",
            titleEn = "Treasure of Paradise (Hawqalah)",
            content = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
            count = 100,
            categoryId = "tasbeeh_praise",
            categoryNameAr = "التسبيح والتحميد",
            categoryNameEn = "Praise & Glorification",
            virtue = "كنز من تحت العرش من كنوز الجنة، وباب من أبوابها",
            source = "صحيح البخاري ومسلم"
        ),
        BuiltInDhikr(
            id = "tb_4",
            title = "سبحان الله وبحمده عدد خلقه",
            titleEn = "Subhanallah 'adada khalqihi",
            content = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، عَدَدَ خَلْقِهِ، وَرِضَا نَفْسِهِ، وَزِنَةَ عَرْشِهِ، وَمِدَادَ كَلِمَاتِهِ",
            count = 3,
            categoryId = "tasbeeh_praise",
            categoryNameAr = "التسبيح والتحميد",
            categoryNameEn = "Praise & Glorification",
            virtue = "تزن وتعدل عبادة ساعات طوال من الذكر والتسبيح",
            source = "صحيح مسلم (2726)"
        ),
        BuiltInDhikr(
            id = "tb_5",
            title = "لا إله إلا الله وحده لا شريك له (مائة مرة)",
            titleEn = "La ilaha illallah 100x",
            content = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            count = 100,
            categoryId = "tasbeeh_praise",
            categoryNameAr = "التسبيح والتحميد",
            categoryNameEn = "Praise & Glorification",
            virtue = "كانت له عدل عشر رقاب، وكتبت له مئة حسنة ومحيت عنه مئة سيئة وكانت له حرزاً من الشيطان",
            source = "صحيح البخاري (3293) ومسلم (2691)"
        ),

        // أدعية قرآنية ونبوية
        BuiltInDhikr(
            id = "qd_1",
            title = "دعاء خيري الدنيا والآخرة",
            titleEn = "Dua for good of Dunya & Akhirah",
            content = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
            count = 3,
            categoryId = "quranic_duas",
            categoryNameAr = "أدعية قرآنية ونبوية",
            categoryNameEn = "Quranic & Prophetic Duas",
            virtue = "أكثر دعاء كان يدعو به النبي صلى الله عليه وسلم",
            source = "سورة البقرة: 201، وصحيح البخاري (6389)"
        ),
        BuiltInDhikr(
            id = "qd_2",
            title = "دعاء الثبات على الدين",
            titleEn = "Dua for steadfastness on Faith",
            content = "يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَىٰ دِينِكَ",
            count = 3,
            categoryId = "quranic_duas",
            categoryNameAr = "أدعية قرآنية ونبوية",
            categoryNameEn = "Quranic & Prophetic Duas",
            virtue = "كان من أكثر دعاء النبي ﷺ لتقلب قلوب العباد",
            source = "سنن الترمذي (2140) وصححه الألباني"
        ),
        BuiltInDhikr(
            id = "qd_3",
            title = "دعاء تفريج الكرب والهم والغم",
            titleEn = "Dua for relief from distress",
            content = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ، وَغَلَبَةِ الرِّجَالِ",
            count = 1,
            categoryId = "quranic_duas",
            categoryNameAr = "أدعية قرآنية ونبوية",
            categoryNameEn = "Quranic & Prophetic Duas",
            virtue = "دعاء نبوي جامع لإذهاب الهموم وسداد الديون",
            source = "صحيح البخاري (2893)"
        ),
        BuiltInDhikr(
            id = "qd_4",
            title = "دعاء الهداية والتقى والعفاف والغنى",
            titleEn = "Dua for guidance, piety and chastity",
            content = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْهُدَىٰ وَالتُّقَىٰ وَالْعَفَافَ وَالْغِنَىٰ",
            count = 1,
            categoryId = "quranic_duas",
            categoryNameAr = "أدعية قرآنية ونبوية",
            categoryNameEn = "Quranic & Prophetic Duas",
            virtue = "جمع صلاح الدين والدنيا بكلمات موجزة",
            source = "صحيح مسلم (2721)"
        ),
        BuiltInDhikr(
            id = "qd_5",
            title = "دعاء الوالدين",
            titleEn = "Dua for parents",
            content = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا ﴿٢٤﴾ رَبَّنَا اغْفِرْ لِي وَلِوَالِدَيَّ وَلِلْمُؤْمِنِينَ يَوْمَ يَقُومُ الْحِسَابُ",
            count = 3,
            categoryId = "quranic_duas",
            categoryNameAr = "أدعية قرآنية ونبوية",
            categoryNameEn = "Quranic & Prophetic Duas",
            virtue = "بر الوالدين بالدعاء لهما في الحياة وبعد الممات",
            source = "سورة الإسراء: 24، وسورة إبراهيم: 41"
        ),
        BuiltInDhikr(
            id = "qd_6",
            title = "دعاء الاستخارة النبوية",
            titleEn = "Istikharah Prayer",
            content = "اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ، وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ، وَأَسْأَلُكَ مِنْ فَضْلِكَ الْعَظِيمِ، فَإِنَّكَ تَقْدِرُ وَلَا أَقْدِرُ، وَتَعْلَمُ وَلَا أَعْلَمُ، وَأَنْتَ عَلَّامُ الْغُيُوبِ...",
            count = 1,
            categoryId = "quranic_duas",
            categoryNameAr = "أدعية قرآنية ونبوية",
            categoryNameEn = "Quranic & Prophetic Duas",
            virtue = "تفويض الأمر إلى الله في كل شأن",
            source = "صحيح البخاري (1166)"
        )
    )
}
