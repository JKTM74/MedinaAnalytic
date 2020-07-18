package com.analitic.enums;

import java.util.List;

/**
 * Enum для ассоциации специальности врача с названием листа в Excel файле.
 * Первое - название листа, второе - специальности врачей, который относятся к этому листу.
 */
public enum SpecialitiesEnum {
    LOR("Оториноларингология", List.of("ЛОР", "Медицинская сестра лор-кабинета")),
    Terapevt("Терапия+", List.of("Невролог-кинезиолог", "Терапевт-Гастроэнтеролог", "Терапевт-Эндокринолог", "Фельдшер", "Терапевт")),
    Ginekolog("Гинекология", List.of("Акушер-гинеколог", "Гинеколог-Ультразвуколог")),
    Hirurg("Хирургия", List.of("Хирург-Ультразвуколог", "Детский хирург")),
    PsihNark("Психиатрия", List.of("Психиатр-нарколог", "Психиатр", "Нарколог")),
    Massage("МАССАЖ", List.of("Массажист"));


    public final String name;
    public final List<String> anySpecialtyNames;

    SpecialitiesEnum(String name, List<String> anySpecialtyNames){
        this.name = name;
        this.anySpecialtyNames = anySpecialtyNames;
    }
}