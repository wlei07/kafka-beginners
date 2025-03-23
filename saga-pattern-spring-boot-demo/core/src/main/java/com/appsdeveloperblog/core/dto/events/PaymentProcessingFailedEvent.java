package com.appsdeveloperblog.core.dto.events;

import java.util.UUID;

public record PaymentProcessingFailedEvent(UUID orderId, UUID productId, Integer productQuantity) {
}
