package com.wqy.campusbbs.service;

import com.wqy.campusbbs.dto.PaginationDTO;
import com.wqy.campusbbs.dto.QuestionDTO;
import com.wqy.campusbbs.dto.QuestionQueryDTO;
import com.wqy.campusbbs.dto.UserDTO;
import com.wqy.campusbbs.enums.UserRoleEnum;
import com.wqy.campusbbs.exception.CustomizeErrorCode;
import com.wqy.campusbbs.exception.CustomizeException;
import com.wqy.campusbbs.mapper.*;
import com.wqy.campusbbs.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(String search, Integer page, Integer size) {
        if (StringUtils.isNotBlank(search)) {
            search = search.replaceAll(" ", "|");
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        int totalPage;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);
        int offset = size * (page - 1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        return getPaginationDTO(paginationDTO, questions);
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        int totalPage;
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(example);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);
        Integer offset = size * (page - 1);
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria().andCreatorEqualTo(userId);
        example1.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example1, new RowBounds(offset, size));
        return getPaginationDTO(paginationDTO, questions);
    }

    private PaginationDTO getPaginationDTO(PaginationDTO paginationDTO, List<Question> questions) {
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            if (user.getRole() == UserRoleEnum.STUDENT.getType()) {
                userDTO.setRoleName(UserRoleEnum.STUDENT.getName());
                StudentClass studentClass = studentClassMapper.selectByPrimaryKey(userDTO.getBelongToId());
                userDTO.setStudentClass(studentClass);
                Specialty specialty = specialtyMapper.selectByPrimaryKey(studentClass.getSpecialtyId());
                userDTO.setSpecialty(specialty);
            }
            if (user.getRole() == UserRoleEnum.ADMINISTRATOR.getType()) {
                userDTO.setRoleName(UserRoleEnum.ADMINISTRATOR.getName());
                Specialty specialty = new Specialty();
                specialty.setName("");
                userDTO.setSpecialty(specialty);
                StudentClass studentClass = new StudentClass();
                studentClass.setClassName("");
                userDTO.setStudentClass(studentClass);
            }
            if (user.getRole() == UserRoleEnum.TEACHER.getType()) {
                userDTO.setRoleName(UserRoleEnum.TEACHER.getName());
                Specialty specialty = specialtyMapper.selectByPrimaryKey(userDTO.getBelongToId());
                userDTO.setSpecialty(specialty);
                StudentClass studentClass = new StudentClass();
                studentClass.setClassName("");
                userDTO.setStudentClass(studentClass);
            }
            questionDTO.setUser(userDTO);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        if (user.getRole() == UserRoleEnum.STUDENT.getType()) {
            userDTO.setRoleName(UserRoleEnum.STUDENT.getName());
            StudentClass studentClass = studentClassMapper.selectByPrimaryKey(userDTO.getBelongToId());
            userDTO.setStudentClass(studentClass);
            Specialty specialty = specialtyMapper.selectByPrimaryKey(studentClass.getSpecialtyId());
            userDTO.setSpecialty(specialty);
        }
        if (user.getRole() == UserRoleEnum.ADMINISTRATOR.getType()) {
            userDTO.setRoleName(UserRoleEnum.ADMINISTRATOR.getName());
        }
        if (user.getRole() == UserRoleEnum.TEACHER.getType()) {
            userDTO.setRoleName(UserRoleEnum.TEACHER.getName());
            Specialty specialty = specialtyMapper.selectByPrimaryKey(userDTO.getBelongToId());
            userDTO.setSpecialty(specialty);
        }
        questionDTO.setUser(userDTO);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        } else {
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (update != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String regexpTag = queryDTO.getTag().replaceAll(",|，| ", "|");
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
