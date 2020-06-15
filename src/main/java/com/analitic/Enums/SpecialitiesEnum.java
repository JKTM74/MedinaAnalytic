package com.analitic.Enums;

import java.util.Arrays;
import java.util.List;

public enum SpecialitiesEnum {
    LOR("Оториноларингология", List.of("ЛОР", "Медицинская сестра лор-кабинета")),
    Nevrolog("Неврология", List.of("Невролог-кинезиолог")),
    Terapevt("Терапия", List.of("Терапевт-Гастроэнтеролог", "Терапевт-Эндокринолог", "Фельдшер")),
    Ginekolog("Гинекология", List.of("Акушер-гинеколог", "Гинеколог-Ультразвуколог")),
    Hirurg("Хирургия", List.of("Хирург-Ультразвуколог", "Детский хирург")),
    UZI("УЗИ", List.of("Ультразвуколог"));


    public final String name;
    public final List<String> anySpecialtyNames;

    SpecialitiesEnum(String name, List<String> anySpecialtyNames){
        this.name = name;
        this.anySpecialtyNames = anySpecialtyNames;
    }
}