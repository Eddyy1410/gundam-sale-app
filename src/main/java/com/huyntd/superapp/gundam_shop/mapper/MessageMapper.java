package com.huyntd.superapp.gundam_shop.mapper;

import com.huyntd.superapp.gundam_shop.dto.request.MessageRequest;
import com.huyntd.superapp.gundam_shop.dto.response.MessageResponse;
import com.huyntd.superapp.gundam_shop.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "sender.fullName", target = "senderName")
    @Mapping(source = "sender.id", target = "senderId")
    MessageResponse toMessageResponse(Message message);

    // Chỉ ánh xạ content, bỏ qua các mối quan hệ phức tạp --> xử lý trong service sau
    @Mapping(target = "conversation", ignore = true)
    @Mapping(target = "sender", ignore = true)
    Message toMessage(MessageRequest request);
}
