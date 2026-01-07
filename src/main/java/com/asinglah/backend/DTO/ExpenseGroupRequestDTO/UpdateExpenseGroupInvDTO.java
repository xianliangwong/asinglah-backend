package com.asinglah.backend.DTO.ExpenseGroupRequestDTO;

public record UpdateExpenseGroupInvDTO(
    long groupId,
    long userId,
    long statusId
) {

}
