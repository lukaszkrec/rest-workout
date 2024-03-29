package com.example.studentjpaexample.feture.person;

import com.example.studentjpaexample.BaseIntegrationTest;
import com.example.studentjpaexmaple.domain.address.dto.AddressDto;
import com.example.studentjpaexmaple.domain.person.PersonRepository;
import com.example.studentjpaexmaple.domain.person.dto.PersonDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PersonIntegrationTest extends BaseIntegrationTest implements PersonTestDataProvider {

    private static final String PERSON_CONTROLLER_PATH = "/person";

    @Autowired
    private PersonRepository personRepository;


    @Test
    void should_create_person_and_save_when_all_perams_are_correct() throws Exception {
        // given
        String str = "2000-17-02";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");
        LocalDate localDate = LocalDate.parse(str, formatter);


        AddressDto address1 = new AddressDto(UUID.randomUUID(), "someStreetname1", "6423-13212", "102b", "2", true);
        AddressDto address2 = new AddressDto(UUID.randomUUID(), "someStreetname2", "6423-13212", "102b", "2", false);

        PersonDto personDto = new PersonDto(UUID.randomUUID(),
                "John",
                "Doe",
                localDate,
                address1,
                List.of(address1, address2)
        );

        //when
        ResultActions response = mockMvc.perform(post(PERSON_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(personDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(personDto.getLastName()))
                .andExpect(jsonPath("$.defaultAddress").value(personDto.getDefaultAddress()))
                .andExpect(jsonPath("$.addresses").value(personDto.getAddresses()));

    }

    @Test
    void testGetAllPersonsShouldReturn() throws Exception {
        //given
        personRepository.saveAll(prepareMockData());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(PERSON_CONTROLLER_PATH + "/all"));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.person").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));
    }

//    @Test
//    void testGetAllPersonsShouldReturn2() throws Exception {
//        //given
//        personRepository.saveAll(PersonTestDataProvider.prepareMockData());
//
//        //when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(PERSON_CONTROLLER_PATH + "/all"));
//
//        //then
//        PersonListResponse personListResponse = asObject(resultActions, PersonListResponse.class);
//
//        Assertions.assertAll(
//                () -> Assertions.assertEquals(resultActions.andReturn().getResponse().getStatus(), 200),
//                () -> Assertions.assertNotNull(personListResponse),
//                () -> Assertions.assertEquals(personListResponse.getPersons().size(), 3),
//                () -> Assertions.assertNotNull(personListResponse.getPersons().get(0).getFirstName(), "Mateusz"));
//    }
//
//    @Test
//    void testGetAllPersonsShouldReturn3() throws Exception {
//        //given
//        personRepository.saveAll(PersonTestDataProvider.prepareMockData());
//        PersonListResponse expectedResponse = PersonTestDataProvider.preparePersonListResponse();
//
//        //when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(PERSON_CONTROLLER_PATH + "/all"));
//
//        //then
//        PersonListResponse response = asObject(resultActions, PersonListResponse.class);
//
//        assertThat(response)
//                .usingRecursiveComparison()
//                .ignoringFields("persons.id")
//                .isEqualTo(expectedResponse);
//    }
//
//
//    @Test
//    void shouldReturnAllPersons() throws Exception {
//        // given
//        List<PersonDto> studentList = List.of(
//                new PersonDto(1L, "John", "Doe", LocalDate.of(1999, 10, 12), new AddressDto(), Collections.emptyList()),
//                new PersonDto(2L, "Jane", "Doe", LocalDate.of(2000, 1, 1), new AddressDto(), Collections.emptyList())
//        );
//        given(personService.getAllPersons()).willReturn(studentList);
//
//        // when
//        MvcResult result = mockMvc.perform(get("/students"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // then
//        String responseJson = result.getResponse().getContentAsString();
//        List<PersonDto> responseList = objectMapper.readValue(responseJson, new TypeReference<>() {
//        });
//        assertThat(responseList).isEqualTo(studentList);
//    }
//
//    @Test
//    void shouldReturnPersonById() throws Exception {
//        // given
//        Long studentId = 1L;
//        PersonDto person = new PersonDto(1L, "John", "Doe", LocalDate.of(1999, 10, 12), new AddressDto(), Collections.emptyList());
//
//        given(personService.findPersonById(studentId)).willReturn(person);
//
//        // when
//        MvcResult result = mockMvc.perform(get("/students/{studentId}", studentId))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // then
//        String responseJson = result.getResponse().getContentAsString();
//        PersonDto responseStudent = objectMapper.readValue(responseJson, PersonDto.class);
//        assertThat(responseStudent).isEqualTo(person);
//    }
//
//    @Test
//    void shouldReturnPersonByName() throws Exception {
//        // given
//        String studentName = "John";
//        PersonDto personDto = new PersonDto(1L, "John", "Doe", LocalDate.of(1999, 10, 12), new AddressDto(), Collections.emptyList());
//
//        given(personService.findPersonByFirstName(studentName)).willReturn(personDto);
//
//        // when
//        MvcResult result = mockMvc.perform(get("/students/name/{studentName}", studentName))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // then
//        String responseJson = result.getResponse().getContentAsString();
//        PersonDto responseStudent = objectMapper.readValue(responseJson, PersonDto.class);
//        assertThat(responseStudent).isEqualTo(personDto);
//    }
//
//    @Test
//    void shouldReturnAllAdultPerson() throws Exception {
//        // given
//        List<Person> personList = List.of(
//                new Person(1L, "John", "Doe", LocalDate.of(1999, 2, 17), Collections.emptyList()),
//                new Person(1L, "John", "Doe", LocalDate.of(2000, 2, 17), Collections.emptyList())
//        );
//
//        PersonDto personDto1 = new PersonDto(1L, "John", "Doe", LocalDate.of(1999, 10, 12), new AddressDto(), Collections.emptyList());
//        PersonDto personDto2 = new PersonDto(2L, "Jane", "Doe", LocalDate.of(2000, 1, 1), new AddressDto(), Collections.emptyList());
//
//        List<PersonDto> adultStudentList = List.of(personDto1, personDto2);
//        given(personService.getAllAdultsPersons()).willReturn(adultStudentList);
//
//
//        // when
//        MvcResult result = mockMvc.perform(get("/students/isadult"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // then
//        String responseJson = result.getResponse().getContentAsString();
//        List<PersonDto> responseList = objectMapper.readValue(responseJson, new TypeReference<>() {
//        });
//        assertThat(responseList).isEqualTo(adultStudentList);
//    }
//
//    @Test
//    void shouldDeletePersonById() throws Exception {
//        // given
//        Long studentId = 1L;
//        Person person = new Person(1L, "John", "Doe", LocalDate.of(2000, 2, 17), Collections.emptyList());
//        PersonDto personDto1 = new PersonDto(1L, "John", "Doe", LocalDate.of(1999, 10, 12), new AddressDto(), Collections.emptyList());
//        given(personRepository.save(person)).willReturn(person);
//        given(personService.findPersonById(person.getId())).willReturn(personDto1);
//        doNothing().when(personService).deletePersonById(studentId);
//
//        // when
//        mockMvc.perform(MockMvcRequestBuilders.delete("/students/{studentId}", studentId))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    void updatePersonName_returnsUpdatedPerson() throws Exception {
//        // given
//        PersonDto existingPerson = new PersonDto(1L, "John", "Doe", LocalDate.of(1999, 10, 12), new AddressDto(), Collections.emptyList());
//
//        given(personService.updatePersonName(existingPerson)).willReturn(existingPerson);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/students"));
//
//        resultActions
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.[1].students").isArray())
//                .andExpect(jsonPath("$.[1].students.firstName").exists())
//                .andExpect(jsonPath("$.[1].students.firstName").value("Jan"));
//    }
}
