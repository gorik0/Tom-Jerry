package com.gorik.common.extensions


fun String.GetYearFromDate() = this.split("-").firstOrNull()
