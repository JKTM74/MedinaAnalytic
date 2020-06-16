package com.analitic.Enums;

import java.util.List;

public enum SpecialitiesEnum {
    LOR("Оториноларингология", List.of("ЛОР", "Медицинская сестра лор-кабинета")),
    Terapevt("Терапия - неврология", List.of("Невролог-кинезиолог", "Терапевт-Гастроэнтеролог", "Терапевт-Эндокринолог", "Фельдшер", "Терапевт")),
    Ginekolog("Гинекология", List.of("Акушер-гинеколог", "Гинеколог-Ультразвуколог")),
    Hirurg("Хирургия", List.of("Хирург-Ультразвуколог", "Детский хирург")),
    UZI("УЗИ", List.of("Ультразвуколог", "Хирург-Ультразвуколог", "Гинеколог-Ультразвуколог")),
    PsihNark("Психиатрия", List.of("Психиатр-нарколог", "Психиатр", "Нарколог")),
    Massage("МАССАЖ", List.of("Массажист"));


    public final String name;
    public final List<String> anySpecialtyNames;

    SpecialitiesEnum(String name, List<String> anySpecialtyNames){
        this.name = name;
        this.anySpecialtyNames = anySpecialtyNames;
    }
}