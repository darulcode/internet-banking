package git.darul.internet_banking.constants;

import lombok.Getter;

@Getter
public enum StatusTransaction {
    PENDING("Pending"),
    FAILED("Failed"),
    COMPLETED("Completed");

    private String description;

    StatusTransaction(String description) {
        this.description = description;
    }

    public static StatusTransaction fromDescription(String description) {
        for (StatusTransaction cartStatus : StatusTransaction.values()){
            if (cartStatus.description.equals(description)){
                return cartStatus;
            }
        }
        return null;
    }
}
