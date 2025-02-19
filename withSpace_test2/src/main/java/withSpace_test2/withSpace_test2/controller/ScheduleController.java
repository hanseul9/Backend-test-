package withSpace_test2.withSpace_test2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import withSpace_test2.withSpace_test2.domain.space.schedule.Category;
import withSpace_test2.withSpace_test2.domain.space.schedule.Schedule;
import withSpace_test2.withSpace_test2.domain.space.schedule.ToDo;
import withSpace_test2.withSpace_test2.requestdto.schedule.category.CategoryRequestDto;
import withSpace_test2.withSpace_test2.requestdto.schedule.category.CategoryUpdateDto;
import withSpace_test2.withSpace_test2.requestdto.schedule.todo.ToDoCompletedUpdateDto;
import withSpace_test2.withSpace_test2.requestdto.schedule.todo.ToDoDescriptionUpdateDto;
import withSpace_test2.withSpace_test2.requestdto.schedule.todo.ToDoRequestDto;
import withSpace_test2.withSpace_test2.responsedto.schedule.category.CategoryBasicResponse;
import withSpace_test2.withSpace_test2.responsedto.BasicResponse;
import withSpace_test2.withSpace_test2.responsedto.schedule.ScheduleDto;
import withSpace_test2.withSpace_test2.responsedto.schedule.todo.ToDoBasicResponse;
import withSpace_test2.withSpace_test2.service.CategoryService;
import withSpace_test2.withSpace_test2.service.ScheduleService;
import withSpace_test2.withSpace_test2.service.ToDoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private static final int SUCCESS = 200;

    private static final int CREATED = 201;

    private final ScheduleService scheduleService;

    private final CategoryService categoryService;

    private final ToDoService toDoService;

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<BasicResponse> schedule(@PathVariable("scheduleId") Long scheduleId) {
        Optional<Schedule> schedule = scheduleService.findSchedule(scheduleId);
        List<ScheduleDto> collect = schedule.stream().map(s -> new ScheduleDto(schedule.get()))
                .collect(Collectors.toList());
        BasicResponse basicResponse = new BasicResponse<>(collect.size(), "스케줄 요청 성공", collect);

        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryBasicResponse> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        Optional<Schedule> schedule = scheduleService.findSchedule(categoryRequestDto.getScheduleId());
        Category category = new Category(schedule.get(), categoryRequestDto.getTitle());

        Long saveCategoryId = categoryService.makeCategory(category);
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(saveCategoryId, CREATED, "카테고리가 등록되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/category/{categoryId}")
    public ResponseEntity<CategoryBasicResponse> changeCategoryTitle(@PathVariable("categoryId") Long categoryId, @RequestBody CategoryUpdateDto categoryUpdateDto) {
        Long updateCategoryId = categoryService.update(categoryId, categoryUpdateDto.getTitle());
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(updateCategoryId, SUCCESS, "카테고리 제목이 수정되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<CategoryBasicResponse> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.delete(categoryId);
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(SUCCESS, "카테고리가 삭제되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.OK);
    }

    @PostMapping("/todo")
    public ResponseEntity<ToDoBasicResponse> createToDo(@RequestBody ToDoRequestDto toDoRequestDto) {
        Optional<Category> findCategory = categoryService.findCategory(toDoRequestDto.getCategoryId());
        ToDo todo = new ToDo(findCategory.get(), toDoRequestDto.getDescription(), toDoRequestDto.getCompleted(), LocalDateTime.now());

        Long saveToDoId = toDoService.makeTodo(todo);
        ToDoBasicResponse createResponseDto = new ToDoBasicResponse(saveToDoId, CREATED, "할일이 등록되었습니다.");
        return new ResponseEntity<>(createResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/todo/{todoId}")
    public ResponseEntity<ToDoBasicResponse> updateToDoDescription(@PathVariable("todoId") Long todoId, @RequestBody ToDoDescriptionUpdateDto toDoDescriptionUpdateDto) {
        Long updateToDoId = toDoService.updateDescription(todoId, toDoDescriptionUpdateDto.getDescription());
        ToDoBasicResponse toDoBasicResponse = new ToDoBasicResponse(updateToDoId, SUCCESS, "할일이 수정되었습니다.");
        return new ResponseEntity<>(toDoBasicResponse, HttpStatus.OK);
    }

    @PatchMapping("/todo/{todoId}/completed")
    public ResponseEntity<ToDoBasicResponse> updateToDoCompleted(@PathVariable("todoId") Long todoId, @RequestBody ToDoCompletedUpdateDto toDoCompletedUpdateDto) {
        Long updateCompletedId = toDoService.updateCompleted(todoId, toDoCompletedUpdateDto.getCompleted());
        ToDoBasicResponse toDoBasicResponse = new ToDoBasicResponse(updateCompletedId, SUCCESS, "할일 완료여부가 수정되었습니다.");
        return new ResponseEntity<>(toDoBasicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity<ToDoBasicResponse> deleteToDo(@PathVariable("todoId") Long todoId) {
        toDoService.deleteToDo(todoId);
        ToDoBasicResponse toDoBasicResponse = new ToDoBasicResponse(SUCCESS, "할일이 삭제되었습니다.");
        return new ResponseEntity<>(toDoBasicResponse, HttpStatus.OK);
    }
}
