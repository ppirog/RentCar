package org.example;

import lombok.Builder;

@Builder
record User(
        String login,
        String password,
        String role
) {
}
