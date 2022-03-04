package advantage.api.support.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Login {

        @Builder.Default
        private String email = "ricardoveiga.ti@gmail.com";
        @Builder.Default
        @JsonProperty("loginUser")
        private String loginName = "rikveiga";
        @Builder.Default
        @JsonProperty("loginPassword")
        private String password = "Abc123";
}
