package puc.purchase

class PurchaseMessage(val userId: Long, val productId: String, val quantity: Int) {

    override fun toString(): String {
        return "PurchaseMessage{" +
                "userId=" + userId +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                '}'
    }
}