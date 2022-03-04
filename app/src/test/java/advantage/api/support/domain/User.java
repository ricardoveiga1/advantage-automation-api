package advantage.api.support.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Builder.Default
    public String accountType = "USER";
    @Builder.Default
    public String address = "rua teste";
    @Builder.Default
    public boolean allowOffersPromotion = true;
//    @Builder.Default
//    public boolean aobUser = true;
    @Builder.Default
    public String cityName = "Rio de Janeiro";
    @Builder.Default
    public String countryId = "Brazil,br";
    @Builder.Default
    public String email = "ricardoveiga.ti@gmail.com";
    @Builder.Default
    public String firstName = "Ricardo";
    @Builder.Default
    public String lastName = "Veiga";
    @Builder.Default
    public String loginName = "rikveiga";
    @Builder.Default
    public String password = "Abc123";
    @Builder.Default
    public String phoneNumber = "21969416489";
    @Builder.Default
    public String stateProvince = "RJ";
    @Builder.Default
    public String zipcode = "23075130";


}


