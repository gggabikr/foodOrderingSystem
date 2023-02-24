package com.menuit.menuitreplica.domain;

public enum OrderStatus {
    confirming, preparing, ReadyForPickUp, completed, paidInFull, cancelled

    // for table order:
    // preparing -> completed -> painInFull // cancelled

    // for pick up // delivery order:
    // confirming -> preparing -> ReadyForPickUp
    // -> completed -> paidInFull // cancelled
}
