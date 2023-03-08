package withSpace_test2.withSpace_test2.responsedto.member;

import lombok.Data;
import withSpace_test2.withSpace_test2.domain.Member;
import withSpace_test2.withSpace_test2.domain.friend.FriendStatus;
import withSpace_test2.withSpace_test2.responsedto.friend.FriendDto;
import withSpace_test2.withSpace_test2.responsedto.friend.FriendListDto;
import withSpace_test2.withSpace_test2.responsedto.memberTeam.MemberTeamResponseDto;
import withSpace_test2.withSpace_test2.responsedto.team.TeamListDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GetMemberResponseDto {

    private Long id;
    private String email;
    private String password;
    private String memberName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;

//    private MemberSpaceDto memberSpaceDto;

    private int teamCount; //가입되어있는 팀의 갯수
    private List<TeamListDto> teamListDtoList;

    private FriendListDto friendListDto;

    //private List<FriendDto> friendList = new ArrayList<>();




    public GetMemberResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.memberName = member.getMemberName();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
        this.status = member.getStatus();

//        //스페이스 관련
//        this.memberSpace = member.getMemberSpace();

        // 회원의 입장에서 팀들의 정보 담기
        if (member.getMemberTeams() != null) {
            teamListDtoList = member.getMemberTeams().stream()
                    .map(memberTeam -> new TeamListDto(memberTeam))
                    .collect(Collectors.toList());
            teamCount = teamListDtoList.size();
        }


        // 친구요청을 건 상대  && status가 accepted인 경우 friendList에 저장
        friendListDto = new FriendListDto(
                member.getFriendRequester().stream()
                        .filter(friendShip -> friendShip.getStatus() == FriendStatus.ACCEPTED)
                        .map(friendShip -> new FriendDto(friendShip.getFriend()))
                        .collect(Collectors.toList()));
    }

}
