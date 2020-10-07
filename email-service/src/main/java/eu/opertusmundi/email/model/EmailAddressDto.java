package eu.opertusmundi.email.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Email address")
@NoArgsConstructor
public class EmailAddressDto {

    @Schema(description = "Contract name", required = false)
    @Getter
    @Setter
    private String name;

    @Schema(description = "Email address", required = true)
    @NotEmpty
    @Email
    @Getter
    @Setter
    private String address;

    public EmailAddressDto(String address) {
        this.address = address;
    }

    public EmailAddressDto(String address, String name) {
        this.address = address;
        this.name    = name;
    }

    @Override
    public String toString() {
        return "EmailAddress [name=" + this.name + ", address=" + this.address + "]";
    }

}
