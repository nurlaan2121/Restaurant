package restaurant.dto.request;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
@Builder
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChequeReq {
    private List<Long> menuitemList = new ArrayList<>();
}
