//package com.jlaner.project.scheduleTest;
//
//import com.jlaner.project.domain.Member;
//import com.jlaner.project.domain.Post;
//import com.jlaner.project.domain.ScheduleData;
//import com.jlaner.project.dto.PostDto;
//import com.jlaner.project.dto.ScheduleDataDto;
//import com.jlaner.project.repository.MemberRepository;
//import com.jlaner.project.repository.PostRepository;
//import com.jlaner.project.repository.ScheduleRepository;
//import com.jlaner.project.service.MemberService;
//import com.jlaner.project.service.PostService;
//import com.jlaner.project.service.ScheduleService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest
//public class ScheduleDataTest {
//
//    @Autowired
//    ScheduleService scheduleService;
//    @Autowired
//    MemberService memberService;
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    ScheduleRepository scheduleRepository;
//
//    @Autowired
//    PostService postService;
//
//    @Autowired
//    PostRepository postRepository;
//
//
//    @DisplayName("ScheduleData 단위 테스트")
//    @Test
//    void saveScheduleDataTest() throws ParseException {
//        //given
//        Member member = new Member();
//
//        memberRepository.save(member);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, -1);
//        Date saveDate = calendar.getTime();
//
//        Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String stringDate = simpleDateFormat.format(date);
//        String saveStringDate = simpleDateFormat.format(saveDate);
//
//        Date newDate = simpleDateFormat.parse(stringDate);
//        Date newSaveDate = simpleDateFormat.parse(saveStringDate);
//
//        ScheduleDataDto scheduleDataDto = new ScheduleDataDto(
//                member, newDate, true,true,true,true,true,true,true,true,true,true,
//                true,true,"1","2","3","4","5","6","7","8","9","10","11","12"
//                );
//
//        ScheduleDataDto saveScheduleDataDto = new ScheduleDataDto(
//                member, newSaveDate, true,true,true,true,true,true,true,true,true,true,
//                true,true,"10","2","3","4","5","6","7","8","9","10","11","12"
//        );
//
//        //when
//        //저장된 데이터가 있을 때, 없을 때를 구분해 테스트 하기 위해 미리 저장하는 데이터
//        scheduleService.saveScheduleData(saveScheduleDataDto, member);
//
//        //db에 없는 데이터를 저장하는 case
//        scheduleService.scheduleDataSaveOrUpdate(scheduleDataDto, member);
//
//        //저장되어 있는 데이터중 scheduleContent1이 10이니 20으로, checkBOx1을 false로 바꿔서 데이터가 올바르게 업데이트 되는지 검사
//        saveScheduleDataDto.setScheduleContent1("20");
//        saveScheduleDataDto.setCheckBox1(false);
//
//        //db에 있는 데이터를 저장하는 case
//        scheduleService.scheduleDataSaveOrUpdate(saveScheduleDataDto, member);
//
//        ScheduleData updateData = scheduleRepository.findByScheduleDataScheduleDateAndMemberId(saveScheduleDataDto.getScheduleDate(), member.getId()).orElse(null);
//        ScheduleData saveData = scheduleRepository.findByScheduleDataScheduleDateAndMemberId(scheduleDataDto.getScheduleDate(), member.getId()).orElse(null);
//
//        List<ScheduleData> allData = scheduleRepository.findByMemberId(member.getId());
//
//        //then
//        assertThat(updateData.getScheduleContent1()).isEqualTo("20");
//        assertThat(updateData.isCheckBox1()).isEqualTo(false);
//
//        assertThat(saveData.getScheduleContent1()).isEqualTo("1");
//        assertThat(saveData.isCheckBox1()).isEqualTo(true);
//
//        assertThat(allData.size()).isEqualTo(2);
//    }
//
//    @DisplayName("저장된 schedule과 post 데이터를 가져오는 테스트")
//    @Test
//    void checkScheduleAndPostData() throws ParseException {
//
//        //given
//        Member member = new Member();
//
//        memberRepository.save(member);
//
//        Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String stringDate = simpleDateFormat.format(date);
//        Date newDate = simpleDateFormat.parse(stringDate);
//
//        PostDto postDto = new PostDto(member,"테스트", newDate);
//        postService.savePost(postDto, member);
//
//        ScheduleDataDto scheduleDataDto = new ScheduleDataDto(
//                member, newDate, true,true,true,true,true,true,true,true,true,true,
//                true,true,"1","2","3","4","5","6","7","8","9","10","11","12"
//        );
//        scheduleService.saveScheduleData(scheduleDataDto, member);
//
//
//
//
//        //when
//        PostDto byPostDate = postService.findByPostDate(newDate, member.getId());
//        ScheduleDataDto byScheduleDate = scheduleService.findByScheduleDate(newDate, member.getId());
//
//        //then
//        assertThat(byPostDate.getContentData()).isEqualTo(postDto.getContentData());
//        assertThat(byScheduleDate.getScheduleContent1()).isEqualTo("1");
//        assertThat(byScheduleDate.isCheckBox1()).isEqualTo(true);
//
//    }
//}
