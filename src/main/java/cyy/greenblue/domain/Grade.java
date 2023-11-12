package cyy.greenblue.domain;

import lombok.Getter;

@Getter
public enum Grade {

    //등급(적립 퍼센트)
    BRONZE(1),
    SILVER(3),
    GOLD(5),
    PLATINUM(7),
    DIAMOND(10);

    private final int percent;

    Grade(int percent) {
        this.percent = percent;
    }

}
