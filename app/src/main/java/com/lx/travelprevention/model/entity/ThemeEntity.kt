package com.lx.travelprevention.model.entity

enum class ThemeType(val labelName: String, val color: Long) {

    PURPLE("紫色", 0xFF6200EE),
    AZURE("蔚蓝", 0xFF70F3FF),
    FLAMING("火红", 0xFFFF2D51),
    GREEN("嫩绿", 0xFFBDDD22),
    APRICOTYELLOW("杏黄", 0xFFFFA631),
    LILAC("丁香色", 0xFFCCA4E3),
    INDIGO("靛青", 0xFF177CB0),
    AZURITE("石青", 0xFF7BCFA6);

    companion object {

        val defaultThemes
            get() = listOf(
                PURPLE,
                AZURE,
                FLAMING,
                GREEN,
                APRICOTYELLOW,
                LILAC,
                INDIGO,
                AZURITE
            )
    }
}