package EntrenaYa.PushNotifier.DTOs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO {

    protected boolean success = true;
    protected String errormessage;
    protected long id;
    private Object object;
}
