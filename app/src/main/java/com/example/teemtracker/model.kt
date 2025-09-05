package com.example.teemtracker

data class Tm(val id: String = "", val name: String = "")
data class Mem(val id: String = "", val name: String = "")
data class Tsk(val id: String = "", val txt: String = "", val memId: String = "", val memName: String = "")
