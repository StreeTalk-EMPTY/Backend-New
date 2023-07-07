package streetalk.demo.v1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import streetalk.demo.v1.domain.Location;
import streetalk.demo.v1.dto.*;
import streetalk.demo.v1.dto.Post.PostLikeResponseDto;
import streetalk.demo.v1.dto.Post.ScrapLikeResponseDto;
import streetalk.demo.v1.dto.User.*;
import streetalk.demo.v1.service.LocationService;
import streetalk.demo.v1.service.SmsService;
import streetalk.demo.v1.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SmsService smsService;
    private final LocationService locationService;
    private final PolicyRepository policyRepository;

    @GetMapping("/user")
    public ResponseEntity<MessageWithData> getUser(HttpServletRequest req) {
        UserResponseDto data = userService.getUser(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "get User info success!", data), HttpStatus.OK);
    }

    @GetMapping("/user/terms")
    public ResponseEntity<MessageWithData> getTermsOfUse() {
        String termsOfUse = "스트릿톡 이용약관\n" +
                "\n" +
                "**제1조(목적**)\n" +
                "\n" +
                "이 약관은 스트릿톡이 운영하는 애플리케이션에서 제공하는 인터넷 관련 서비스(이하 “서비스”라 한다)를 이용함에 있어 애플리케이션(이하 “앱”이라 한다) 과 이용자의 권리의무 및 책임사항을 규정함을 목적으로 합니다.\n" +
                "\n" +
                "**제2조(정의)**\n" +
                "\n" +
                "① “앱” 란 서비스 제공의 주체를 말합니다\n" +
                "\n" +
                "② “이용자”이란 이 약관에 따라 서비스를 이용하는 회원 및 비회원을 말합니다.\n" +
                "\n" +
                "③ ‘회원’이라 함은 서비스에 회원등록을 한 자로서, 계속적으로 서비스를 이용할 수 있는 자를 말합니다.\n" +
                "\n" +
                "④ ‘비회원’이라 함은 회원에 가입하지 않고 서비스를 이용하는 자를 말합니다.\n" +
                "\n" +
                "**제3조 (약관 등의 명시와 설명 및 개정)**\n" +
                "\n" +
                "① “앱”은 이 약관의 내용과 개인정보관리책임자등을 이용자가 쉽게 알 수 있도록 스트릿톡 앱에 게시합니다. 다만, 약관의 내용은 이용자가 연결화면을 통하여 볼 수 있도록 할 수 있습니다.\n" +
                "\n" +
                "② “앱”은 이용자가 약관에 동의하기에 앞서 중요한 내용을 이용자가 이해할 수 있도록 별도의 연결화면 또는 팝업화면 등을 제공하여 이용자의 확인을 구하여야 합니다.\n" +
                "\n" +
                "③ “앱”은 「전자상거래 등에서의 소비자보호에 관한 법률」, 「약관의 규제\n" +
                "\n" +
                "에 관한 법률」, 「전자문서 및 전자거래기본법」, 「전자금융거래법」,\n" +
                "\n" +
                "「전자서명법」, 「정보통신망 이용촉진 및 정보보호 등에 관한 법률」,\n" +
                "\n" +
                "「방문판매 등에 관한 법률」, 「소비자기본법」 등 관련 법을 위배하지 않\n" +
                "\n" +
                "는 범위에서 이 약관을 개정할 수 있습니다.\n" +
                "\n" +
                "④ “앱”이 약관을 개정할 경우에는 적용일자 및 개정사유를 명시하여 현행약\n" +
                "\n" +
                "관과 함께 앱의 초기화면에 그 적용일자 7일 이전부터 적용일자 전일까지\n" +
                "\n" +
                "공지합니다. 다만, 이용자에게 불리하게 약관내용을 변경하는 경우에는 최소\n" +
                "\n" +
                "한 30일 이상의 사전 유예기간을 두고 공지합니다. 이 경우 \"앱“은 개정 전 내용과 개정 후 내용을 명확하게 비교하여 이용자가 알기 쉽도록 표시합니다.\n" +
                "\n" +
                "⑤ 이 약관에서 정하지 아니한 사항과 이 약관의 해석에 관하여는 전자상거래등에서의 소비자보호에 관한 법률, 약관의 규제 등에 관한 법률, 공정거래위원회가 정하는 전자상거래 등에서의 소비자 보호지침 및 관계법령 또는 상관례에 따릅니다.\n" +
                "\n" +
                "**제4조(서비스의 제공 및 변경)**\n" +
                "\n" +
                "① “앱”은 다음과 같은 업무를 수행합니다.\n" +
                "\n" +
                "1. 커뮤니티 서비스\n" +
                "\n" +
                "2. 소상공인 관련 정보 제공 서비스\n" +
                "\n" +
                "3. 할인, 이벤트. 프로모션, 광고 정보 제공 서비스\n" +
                "\n" +
                "② “앱”은 운영 과정에서 발생하는 기술상의 필요에 따라 제공하고 있는 서비스 이용에 차이를 둘 수 있습니다.\n" +
                "\n" +
                "**제5조(서비스의 중단)**\n" +
                "\n" +
                "① “앱”은 컴퓨터 등 정보통신설비의 보수점검 교체 및 고장, 통신의 두절 등의 사유가 발생한 경우에는 서비스의 제공을 일시적으로 중단할 수 있습니다.\n" +
                "\n" +
                ".\n" +
                "\n" +
                "**제6조(회원가입)**\n" +
                "\n" +
                "① 이용자는 “앱”이 정한 가입 양식에 따라 회원정보를 기입한 후 이 약관에\n" +
                "\n" +
                "동의한다는 의사표시를 함으로서 회원가입을 신청합니다.\n" +
                "\n" +
                "② “앱”은 제1항과 같이 회원으로 가입할 것을 신청한 이용자 중 다음 각 호에 해당하지 않는 한 회원으로 등록합니다.\n" +
                "\n" +
                "1. 가입신청자가 이 약관 제7조제2항에 의하여 이전에 회원자격을 상실한    적이 있는 경우\n" +
                "\n" +
                "2. 등록 내용에 허위, 기재누락, 오기가 있는 경우\n" +
                "\n" +
                "3. 기타 회원으로 등록하는 것이 앱의 기술상 현저히 지장이 있다고 판단\n" +
                "\n" +
                "되는 경우\n" +
                "\n" +
                "③ 회원가입계약의 성립 시기는 “앱”의 내부 회원가입 화면의 가입 양식에 따라 회원정보를 기입한 후 필수 약관에 동의한다는 의사표시를 한 비회원의 이용신청에 대하여, 서비스 화면에 이용승낙을 표시하는 방법 등으로 의사표시가 회원에게 도달한 시점으로 합니다.\n" +
                "\n" +
                "④ 회원은 회원가입 시 등록한 사항에 변경이 있는 경우, 상당한 기간 이내에 “앱”에 대하여 회원정보 수정 등의 방법으로 그 변경사항을 알려야 합니\n" +
                "\n" +
                "다.\n" +
                "\n" +
                "⑤ “앱”은 부정사용방지 및 본인확인을 위하여 회원에게 사업자 인증을 요청할 수 있습니다\n" +
                "\n" +
                "**제7조(회원 탈퇴 및 자격 상실 등)**\n" +
                "\n" +
                "① 회원은 “앱”에 언제든지 탈퇴를 요청할 수 있으며 “앱”은 즉시 회원탈\n" +
                "\n" +
                "퇴를 처리합니다.\n" +
                "\n" +
                "② 회원이 다음 각 호의 사유에 해당하는 경우, “몰”은 회원자격을 제한 및\n" +
                "\n" +
                "정지시킬 수 있습니다.\n" +
                "\n" +
                "1. 가입 신청 시에 허위 내용을 등록한 경우\n" +
                "\n" +
                "2. 다른 사람의 “앱” 이용을 방해하거나 그 정보를 도용하는 등 앱 내의   질서를 위반하는 경우\n" +
                "\n" +
                "3. “앱”을 이용하여 법령 또는 이 약관이 금지하거나 공서양속에 반하는 행위를 하는 경우\n" +
                "\n" +
                "③ “앱”이 회원자격을 상실시키는 경우에는 회원등록을 말소합니다.\n" +
                "\n" +
                "**제8조(회원에 대한 통지)**\n" +
                "\n" +
                "① “앱”이 회원에 대한 통지를 하는 경우, 회원이 “앱”과 미리 약정하여 지\n" +
                "\n" +
                "정한 전자우편 주소, 개인 연락처, 푸시 알림으로 할 수 있습니다.\n" +
                "\n" +
                "② “앱”은 불특정다수 회원에 대한 통지의 경우 1주일 이상 “앱” 게시판에\n" +
                "\n" +
                "게시함으로서 개별 통지에 갈음할 수 있습니다. 다만 회원 본인의 이용과 관련하여 중대한 영향을 미치는 사항에 대하여는 개별 통지를 합니다\n" +
                "\n" +
                "**제9조(개인정보보호)**\n" +
                "\n" +
                "① “앱”은 이용자의 개인정보 수집시 서비스제공을 위하여 필요한 범위에서\n" +
                "\n" +
                "최소한의 개인정보를 수집합니다.\n" +
                "\n" +
                "② “앱”은 용자의 개인정보를 수집, 이용하는 때에는 당해 이용자에게 그 목적을 고지하고 동의를 받습니다.\n" +
                "\n" +
                "③ 앱 은 수집된 개인정보를 목적외의 용도로 이용할 수 없으며, 새로운 이용목적이 발생한 경우 또는 제3자에게 제공하는 경우에는 이용, 제공단계에서 당해 이용자에게 그 목적을 고지하고 동의를 받습니다. 다만, 관련 법령\n" +
                "\n" +
                "에 달리 정함이 있는 경우에는 예외로 합니다.\n" +
                "\n" +
                "④ 이용자는 언제든지 “앱”이 가지고 있는 자신의 개인정보에 대해 열람 및\n" +
                "\n" +
                "오류정정을 요구할 수 있으며 “앱”은 이에 대해 지체 없이 필요한 조치를\n" +
                "\n" +
                "취할 의무를 집니다. 이용자가 오류의 정정을 요구한 경우에는 “앱”은 그\n" +
                "\n" +
                "오류를 정정할 때까지 당해 개인정보를 이용하지 않습니다.\n" +
                "\n" +
                "⑤ “앱”은 개인정보 보호를 위하여 이용자의 개인정보를 취급하는 자를 최소한으로 제한합니다\n" +
                "\n" +
                "⑥ “앱”은 개인정보의 수집 이용 제공에 관한 동의 란을 미리 선택한 것으로 설정해두지 않습니다. 또한 개인정보의 수집 이용 제공에 관한 이용자의 동의 거절 시 제한되는 서비스를 구체적으로 명시하고, 필수수집항목이 아닌 개인정보의 수집 이용 제공에 관한 이용자의 동의 거절을 이유로 회원가입 등 서비스 제공을 제한하거나 거절하지 않습니다.\n" +
                "\n" +
                "**제10조(“앱“의 의무)**\n" +
                "\n" +
                "① “앱”은 법령과 이 약관이 금지하거나 공서양속에 반하는 행위를 하지 않\n" +
                "\n" +
                "으며 이 약관이 정하는 바에 따라 지속적이고, 안정적으로 서비스를 제공 하는데 최선을 다하여야 합니다.\n" +
                "\n" +
                "② “앱”은 이용자가 안전하게 인터넷 서비스를 이용할 수 있도록 이용자의\n" +
                "\n" +
                "개인정보(신용정보 포함)보호를 위한 보안 시스템을 갖추어야 합니다.\n" +
                "\n" +
                "**제11조(회원의 ID 및 비밀번호에 대한 의무)**\n" +
                "\n" +
                "① ID와 비밀번호에 관한 관리책임은 회원에게 있습니다.\n" +
                "\n" +
                "② 회원은 자신의 ID 및 비밀번호를 제3자에게 이용하게 해서는 안됩니다.\n" +
                "\n" +
                "③ 회원이 자신의 ID 및 비밀번호를 도난당하거나 제3자가 사용하고 있음을 인지한 경우에는 바로 “앱”에 통보하고 “앱”의 안내가 있는 경우에는 그 에 따라야 합니다.\n" +
                "\n" +
                "**제12조(이용자의 의무)** **이용자는 다음 행위를 하여서는 안 됩니다.**\n" +
                "\n" +
                "1. 신청 또는 변경시 허위 내용의 등록\n" +
                "\n" +
                "2. 타인의 정보 도용\n" +
                "\n" +
                "3. “앱”에 게시된 정보의 변경\n" +
                "\n" +
                "4. “앱”이 정한 정보 이외의 정보(컴퓨터 프로그램 등) 등의 송신 또는 게\n" +
                "\n" +
                "시\n" +
                "\n" +
                "5. “앱” 기타 제3자의 저작권 등 지적재산권에 대한 침해\n" +
                "\n" +
                "6. “앱” 기타 제3자의 명예를 손상시키거나 업무를 방해하는 행위\n" +
                "\n" +
                "7. 외설 또는 폭력적인 메시지, 화상, 음성, 기타 공서양속에 반하는 정보를\n" +
                "\n" +
                "몰에 공개 또는 게시하는 행위\n" +
                "\n" +
                "**제13조(저작권의 귀속 및 이용제한)**\n" +
                "\n" +
                "① “앱“이 작성한 저작물에 대한 저작권 기타 지적재산권은 ”앱“에 귀속합\n" +
                "\n" +
                "니다.\n" +
                "\n" +
                "② 이용자는 “앱”을 이용함으로써 얻은 정보 중 “앱”에게 지적재산권이 귀\n" +
                "\n" +
                "속된 정보를 “앱”의 사전 승낙 없이 복제, 송신, 출판, 배포, 방송 기타 방\n" +
                "\n" +
                "법에 의하여 영리목적으로 이용하거나 제3자에게 이용하게 하여서는 안됩니\n" +
                "\n" +
                "다.\n" +
                "\n" +
                "③ “앱”은 약정에 따라 이용자에게 귀속된 저작권을 사용하는 경우 당해 이\n" +
                "\n" +
                "용자에게 통보하여야 합니다.\n" +
                "\n" +
                "**제14조(분쟁해결)**\n" +
                "\n" +
                "① “앱”은 이용자로부터 제출되는 불만사항 및 의견은 우선적으로 그 사항을\n" +
                "\n" +
                "처리합니다. 다만, 신속한 처리가 곤란한 경우에는 이용자에게 그 사유와 처\n" +
                "\n" +
                "리일정을 즉시 통보해 드립니다.\n" +
                "\n" +
                "**제15조(재판권 및 준거법)**\n" +
                "\n" +
                "① “앱”과 이용자 간에 발생한 분쟁에 관한 소송은 대한민국 서울 중앙지방법원을 관할 법원으로 합니다. 다만, 제소 당시 이용자의 주소 또는 거소가 분명하지\n" +
                "\n" +
                "않거나 외국 거주자의 경우에는 민사소송법상의 관할법원에 제기합니다.\n" +
                "\n" +
                "② “앱”과 이용자 간에 제기된 전자상거래 소송에는 한국법을 적용합니다.";

        String privatePolicy = "개인정보 처리방침\n" +
                "\n" +
                "< 스트릿톡 >('스트릿톡'이하 '스트릿톡')은(는) 「개인정보 보호법」 제30조에 따라 정보주체의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 처리방침을 수립·공개합니다.\n" +
                "\n" +
                "○ 이 개인정보처리방침은 2022년 10월 1부터 적용됩니다.\n" +
                "\n" +
                "제1조(개인정보의 처리 목적)\n" +
                "\n" +
                "< 스트릿톡 >('스트릿톡'이하 '스트릿톡')은(는) 다음의 목적을 위하여 개인정보를 처리합니다. 처리하고 있는 개인정보는 다음의 목적 이외의 용도로는 이용되지 않으며 이용 목적이 변경되는 경우에는 「개인정보 보호법」 제18조에 따라 별도의 동의를 받는 등 필요한 조치를 이행할 예정입니다.\n" +
                "\n" +
                "1. 홈페이지 회원가입 및 관리\n" +
                "\n" +
                "회원 가입의사 확인, 회원자격 유지·관리 목적으로 개인정보를 처리합니다.\n" +
                "\n" +
                "1. 민원사무 처리\n" +
                "\n" +
                "민원인의 신원 확인, 민원사항 확인, 사실조사를 위한 연락·통지, 처리결과 통보 목적으로 개인정보를 처리합니다.\n" +
                "\n" +
                "1. 재화 또는 서비스 제공\n" +
                "\n" +
                "서비스 제공, 콘텐츠 제공, 본인인증을 목적으로 개인정보를 처리합니다.\n" +
                "\n" +
                "제2조(개인정보의 처리 및 보유 기간)\n" +
                "\n" +
                "① < 스트릿톡 >은(는) 법령에 따른 개인정보 보유·이용기간 또는 정보주체로부터 개인정보를 수집 시에 동의받은 개인정보 보유·이용기간 내에서 개인정보를 처리·보유합니다.\n" +
                "\n" +
                "② 각각의 개인정보 처리 및 보유 기간은 다음과 같습니다.\n" +
                "\n" +
                "1.<홈페이지 회원가입 및 관리>\n" +
                "<홈페이지 회원가입 및 관리>와 관련한 개인정보는 수집.이용에 관한 동의일로부터<3년>까지 위 이용목적을 위하여 보유.이용됩니다.\n" +
                "보유근거 : 홈페이지 초기 회원가입 및 이용\n" +
                "관련법령 : 계약 또는 청약철회 등에 관한 기록 : 5년\n" +
                "\n" +
                "제3조(처리하는 개인정보의 항목)\n" +
                "\n" +
                "① < 스트릿톡 >은(는) 다음의 개인정보 항목을 처리하고 있습니다.\n" +
                "\n" +
                "1< 홈페이지 회원가입 및 관리 >\n" +
                "필수항목 : 휴대전화번호, 비밀번호, 로그인ID, 성별, 생년월일, 이름\n" +
                "\n" +
                "제4조(개인정보의 파기절차 및 파기방법)\n" +
                "\n" +
                "① < 스트릿톡 > 은(는) 개인정보 보유기간의 경과, 처리목적 달성 등 개인정보가 불필요하게 되었을 때에는 지체없이 해당 개인정보를 파기합니다.\n" +
                "\n" +
                "② 정보주체로부터 동의받은 개인정보 보유기간이 경과하거나 처리목적이 달성되었음에도 불구하고 다른 법령에 따라 개인정보를 계속 보존하여야 하는 경우에는, 해당 개인정보를 별도의 데이터베이스(DB)로 옮기거나 보관장소를 달리하여 보존합니다.\n" +
                "\n" +
                "③ 개인정보 파기의 절차 및 방법은 다음과 같습니다.\n" +
                "\n" +
                "1. 파기절차\n" +
                "< 스트릿톡 > 은(는) 파기 사유가 발생한 개인정보를 선정하고, < 스트릿톡 > 의 개인정보 보호책임자의 승인을 받아 개인정보를 파기합니다.\n" +
                "2. 파기방법\n" +
                "\n" +
                "전자적 파일 형태의 정보는 기록을 재생할 수 없는 기술적 방법을 사용합니다.\n" +
                "\n" +
                "종이에 출력된 개인정보는 분쇄기로 분쇄하거나 소각을 통하여 파기합니다\n" +
                "\n" +
                "제5조(정보주체와 법정대리인의 권리·의무 및 그 행사방법에 관한 사항)\n" +
                "\n" +
                "① 정보주체는 스트릿톡에 대해 언제든지 개인정보 열람·정정·삭제·처리정지 요구 등의 권리를 행사할 수 있습니다.\n" +
                "\n" +
                "② 제1항에 따른 권리 행사는스트릿톡에 대해 「개인정보 보호법」 시행령 제41조제1항에 따라 서면, 전자우편, 모사전송(FAX) 등을 통하여 하실 수 있으며 스트릿톡은(는) 이에 대해 지체 없이 조치하겠습니다.\n" +
                "\n" +
                "③ 제1항에 따른 권리 행사는 정보주체의 법정대리인이나 위임을 받은 자 등 대리인을 통하여 하실 수 있습니다.이 경우 “개인정보 처리 방법에 관한 고시(제2020-7호)” 별지 제11호 서식에 따른 위임장을 제출하셔야 합니다.\n" +
                "\n" +
                "④ 개인정보 열람 및 처리정지 요구는 「개인정보 보호법」 제35조 제4항, 제37조 제2항에 의하여 정보주체의 권리가 제한 될 수 있습니다.\n" +
                "\n" +
                "⑤ 개인정보의 정정 및 삭제 요구는 다른 법령에서 그 개인정보가 수집 대상으로 명시되어 있는 경우에는 그 삭제를 요구할 수 없습니다.\n" +
                "\n" +
                "⑥ 스트릿톡은(는) 정보주체 권리에 따른 열람의 요구, 정정·삭제의 요구, 처리정지의 요구 시 열람 등 요구를 한 자가 본인이거나 정당한 대리인인지를 확인합니다.\n" +
                "\n" +
                "제6조(개인정보의 안전성 확보조치에 관한 사항)\n" +
                "\n" +
                "< 스트릿톡 >은(는) 개인정보의 안전성 확보를 위해 다음과 같은 조치를 취하고 있습니다.\n" +
                "\n" +
                "1. 정기적인 자체 감사 실시\n" +
                "개인정보 취급 관련 안정성 확보를 위해 정기적(분기 1회)으로 자체 감사를 실시하고 있습니다.\n" +
                "2. 개인정보 취급 직원의 최소화 및 교육\n" +
                "개인정보를 취급하는 직원을 지정하고 담당자에 한정시켜 최소화 하여 개인정보를 관리하는 대책을 시행하고 있습니다.\n" +
                "3. 개인정보의 암호화\n" +
                "이용자의 개인정보는 비밀번호는 암호화 되어 저장 및 관리되고 있어, 본인만이 알 수 있으며 중요한 데이터는 파일 및 전송 데이터를 암호화 하거나 파일 잠금 기능을 사용하는 등의 별도 보안기능을 사용하고 있습니다.\n" +
                "4. 문서보안을 위한 잠금장치 사용\n" +
                "개인정보가 포함된 서류, 보조저장매체 등을 잠금장치가 있는 안전한 장소에 보관하고 있습니다.\n" +
                "\n" +
                "제7조(개인정보를 자동으로 수집하는 장치의 설치·운영 및 그 거부에 관한 사항)\n" +
                "\n" +
                "① 스트릿톡 은(는) 이용자에게 개별적인 맞춤서비스를 제공하기 위해 이용정보를 저장하고 수시로 불러오는 ‘쿠키(cookie)’를 사용합니다.\n" +
                "② 쿠키는 웹사이트를 운영하는데 이용되는 서버(http)가 이용자의 컴퓨터 브라우저에게 보내는 소량의 정보이며 이용자들의 PC 컴퓨터내의 하드디스크에 저장되기도 합니다.\n" +
                "가. 쿠키의 사용 목적 : 이용자가 방문한 각 서비스와 웹 사이트들에 대한 방문 및 이용형태, 인기 검색어, 보안접속 여부, 등을 파악하여 이용자에게 최적화된 정보 제공을 위해 사용됩니다.\n" +
                "나. 쿠키의 설치•운영 및 거부 : 웹브라우저 상단의 도구>인터넷 옵션>개인정보 메뉴의 옵션 설정을 통해 쿠키 저장을 거부 할 수 있습니다.\n" +
                "다. 쿠키 저장을 거부할 경우 맞춤형 서비스 이용에 어려움이 발생할 수 있습니다.\n" +
                "\n" +
                "제8조 (개인정보 보호책임자에 관한 사항)\n" +
                "\n" +
                "① 스트릿톡 은(는) 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제 등을 위하여 아래와 같이 개인정보 보호책임자를 지정하고 있습니다.\n" +
                "\n" +
                "▶ 개인정보 보호책임자\n" +
                "성명 :이주연\n" +
                "연락처 :01091154196, [ljy5587@naver.com](mailto:ljy5587@naver.com),\n" +
                "\n" +
                "② 정보주체께서는 스트릿톡 의 서비스(또는 사업)을 이용하시면서 발생한 모든 개인정보 보호 관련 문의, 불만처리, 피해구제 등에 관한 사항을 개인정보 보호책임자 및 담당부서로 문의하실 수 있습니다. 스트릿톡 은(는) 정보주체의 문의에 대해 지체 없이 답변 및 처리해드릴 것입니다.\n" +
                "\n" +
                "제9조(개인정보의 열람청구를 접수·처리하는 부서)\n" +
                "정보주체는 ｢개인정보 보호법｣ 제35조에 따른 개인정보의 열람 청구를 아래의 부서에 할 수 있습니다.\n" +
                "< 스트릿톡 >은(는) 정보주체의 개인정보 열람청구가 신속하게 처리되도록 노력하겠습니다.\n" +
                "\n" +
                "▶ 개인정보 열람청구 접수·처리 부서\n" +
                "부서명 : 열람청구 접수 · 처리 부서\n" +
                "담당자 : 이주연\n" +
                "연락처 : 010-9115-4196\n" +
                "\n" +
                "제10조(정보주체의 권익침해에 대한 구제방법)\n" +
                "\n" +
                "정보주체는 개인정보침해로 인한 구제를 받기 위하여 개인정보분쟁조정위원회, 한국인터넷진흥원 개인정보침해신고센터 등에 분쟁해결이나 상담 등을 신청할 수 있습니다. 이 밖에 기타 개인정보침해의 신고, 상담에 대하여는 아래의 기관에 문의하시기 바랍니다.\n" +
                "\n" +
                "1. 개인정보분쟁조정위원회 : (국번없이) 1833-6972 ([www.kopico.go.kr](http://www.kopico.go.kr/))\n" +
                "2. 개인정보침해신고센터 : (국번없이) 118 ([privacy.kisa.or.kr](http://privacy.kisa.or.kr/))\n" +
                "3. 대검찰청 : (국번없이) 1301 ([www.spo.go.kr](http://www.spo.go.kr/))\n" +
                "4. 경찰청 : (국번없이) 182 ([ecrm.cyber.go.kr](http://ecrm.cyber.go.kr/))\n" +
                "\n" +
                "「개인정보보호법」제35조(개인정보의 열람), 제36조(개인정보의 정정·삭제), 제37조(개인정보의 처리정지 등)의 규정에 의한 요구에 대 하여 공공기관의 장이 행한 처분 또는 부작위로 인하여 권리 또는 이익의 침해를 받은 자는 행정심판법이 정하는 바에 따라 행정심판을 청구할 수 있습니다.\n" +
                "\n" +
                "※ 행정심판에 대해 자세한 사항은 중앙행정심판위원회([www.simpan.go.kr](http://www.simpan.go.kr/)) 홈페이지를 참고하시기 바랍니다.\n" +
                "\n" +
                "제12조(개인정보 처리방침 변경)\n" +
                "\n" +
                "① 이 개인정보처리방침은 2022년 10월 1부터 적용됩니다.";

//            policyRepository.save(Policy.builder()
//                            .id(1L)
//                            .termsOfUse(termsOfUse)
//                            .privatePolicy(privatePolicy)
//                            .build());

        return new ResponseEntity<>(new MessageWithData(200, true, "get Terms of Use Success", termsOfUse + privatePolicy), HttpStatus.OK);
    }

    @PostMapping("/user/auth")
    public ResponseEntity<MessageWithData> auth(@RequestBody HashMap<String, Object> values) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        String phoneNum = values.get("phoneNum").toString();
        AuthResponseDto data = userService.doAuth(phoneNum);
        return new ResponseEntity<>(new MessageWithData(200, true, "auth Success", data), HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public ResponseEntity<MessageWithData> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto data = userService.login(loginRequestDto);
        return new ResponseEntity<>(new MessageWithData(200, true, "auth Success", data), HttpStatus.OK);
    }

    @PutMapping("/user/refreshToken")
    public ResponseEntity<MessageWithData> refreshToken(HttpServletRequest req) {
        RefreshTokenDto data = userService.refreshToken(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "refreshToken success!", data), HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<MessageOnly> updateUser(HttpServletRequest req, @RequestBody PutUserRequestDto putUserRequestDto){
        userService.updateUser(req, putUserRequestDto);
        return new ResponseEntity<>(new MessageOnly(200, true, "update User Success"), HttpStatus.OK);
    }

//    @GetMapping("/user/{id}/postlike")
//    public ResponseEntity<MessageWithData> getUserPostLike(HttpServletRequest req){
//        List<PostLikeResponseDto>data=userService.getUserPostLike(req);
//        return new ResponseEntity<>(new MessageWithData(200,true,"postupdate",data),HttpStatus.OK);
//    }

    @GetMapping("/user/postLikes")
    public ResponseEntity<MessageWithData> getUserPostLike(HttpServletRequest req){
        List<PostLikeResponseDto> data=userService.getUserPostLike(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "getUserPostLike success!", data), HttpStatus.OK);
    }

    @GetMapping("/user/postScraps")
    public ResponseEntity<MessageWithData> getUserScrapLike(HttpServletRequest req){
        List<ScrapLikeResponseDto> data=userService.getUserScrapLike(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "getUserScrapLike success!", data), HttpStatus.OK);
    }

    @PostMapping("/test")
    public void test(@RequestBody HashMap<String, Object> values) {
        Location data = locationService.getKoLocation(Double.parseDouble(values.get("x").toString()), Double.parseDouble(values.get("y").toString()));
    }
}
