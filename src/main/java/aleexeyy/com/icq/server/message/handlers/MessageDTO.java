package aleexeyy.com.icq.server.message.handlers;

import java.time.LocalDateTime;

public record MessageDTO(String content, LocalDateTime sentAt, String senderUsername) {}