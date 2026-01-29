package com.lbteam.priont.model.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RegionType(val label: String) {
    @SerialName("전국")
    All("전국"),

    @SerialName("서울")
    Seoul("서울"),

    @SerialName("부산")
    Busan("부산"),

    @SerialName("대구")
    Daegu("대구"),

    @SerialName("인천")
    Incheon("인천"),

    @SerialName("광주")
    Gwangju("광주"),

    @SerialName("대전")
    Daejeon("대전"),

    @SerialName("울산")
    Ulsan("울산"),

    @SerialName("수원")
    Suwon("수원"),

    @SerialName("강릉")
    Gangneung("강릉"),

    @SerialName("춘천")
    Chuncheon("춘천"),

    @SerialName("청주")
    Cheongju("청주"),

    @SerialName("전주")
    Jeonju("전주"),

    @SerialName("포항")
    Pohang("포항"),

    @SerialName("제주")
    Jeju("제주"),

    @SerialName("순천")
    Suncheon("순천"),

    @SerialName("안동")
    Andong("안동"),

    @SerialName("창원")
    Changwon("창원"),

    @SerialName("용인")
    Yongin("용인"),

    @SerialName("세종")
    Sejong("세종"),

    @SerialName("성남")
    Seongnam("성남"),

    @SerialName("고양")
    Goyang("고양"),

    @SerialName("천안")
    Cheonan("천안"),

    @SerialName("김해")
    Gimhae("김해");
}