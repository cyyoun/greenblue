package cyy.greenblue.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
    COMPLETE("complete"),
    SUCCESS("success"),
    CANCEL("cancel"),
    CANCEL_COMPLETE("cancelComplete");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}