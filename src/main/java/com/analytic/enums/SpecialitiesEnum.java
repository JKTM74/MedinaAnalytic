package com.analytic.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Enum для ассоциации специальности врача с названием листа в Excel файле.
 * Первое - название листа, второе - специальности врачей, который относятся к этому листу.
 */
public enum SpecialitiesEnum {
    LOR("Оториноларингология", Arrays.asList("ЛОР", "Медицинская сестра лор-кабинета")),
    Terapevt("Терапия+", Arrays.asList("Невролог-кинезиолог", "Терапевт-Гастроэнтеролог", "Терапевт-Эндокринолог", "Фельдшер", "Терапевт")),
    Ginekolog("Гинекология", Arrays.asList("Акушер-гинеколог", "Гинеколог-Ультразвуколог")),
    Hirurg("Хирургия", Arrays.asList("Хирург-Ультразвуколог", "Детский хирург")),
    PsihNark("Психиатрия", Arrays.asList("Психиатр-нарколог", "Психиатр", "Нарколог")),
    Massage("МАССАЖ", Arrays.asList("Массажист"));


    public final String name;
    public final List<String> anySpecialtyNames;

    SpecialitiesEnum(String name, List<String> anySpecialtyNames) {
        this.name = name;
        this.anySpecialtyNames = anySpecialtyNames;
    }
}