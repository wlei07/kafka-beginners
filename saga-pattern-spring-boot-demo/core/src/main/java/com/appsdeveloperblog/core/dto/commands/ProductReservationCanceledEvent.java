package com.appsdeveloperblog.core.dto.commands;

import java.util.UUID;

public record ProductReservationCanceledEvent(UUID productId, UUID orderId) {
}
