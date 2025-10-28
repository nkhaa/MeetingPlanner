Лабораторийн тайлан

**CalendarTest.java**

Энэ нь Calendar классын бүх гол логикыг туршсан.

testAddMeeting_invalidDay_throws() – “2-р сарын 35” гэх боломжгүй өдөр оруулахад TimeConflictException гарч байгаа эсэхийг шалгасан.

testAddMeeting_invalidMonth_throws() – “13-р сар” гэх буруу утгад систем зөв алдаа гаргаж байна.

testAddOverlappingMeetings_sameDay_overlapThrows() – Давхардсан уулзалт (10:00–12:00 ба 11:00–13:00) үүсгэхэд систем алдаа илрүүлж, TimeConflictException шидэж байгааг баталсан.

testBackToBackMeetings_disallowed() – Ар араасаа дараалсан уулзалт (10–11, дараа нь 11–12)  хориглогдож байгааг шалгасан.

testRemoveMeeting_freesSlot() – Уулзалт устгасны дараа тухайн цаг “сул” болж байгааг isBusy() функцээр баталсан.

testPrintAgenda_month_includesMeeting() ба testPrintAgenda_day_includesMeeting() – Сарын болон өдрийн хуваарь хэвлэх функц нь зөв мэдээлэл гаргаж байгааг шалгасан.

**MeetingTest.java**


testSetAndGetValues() – Уулзалтын өдөр, сар, цаг, өрөө, тайлбарыг зөв хадгалж, буцааж байгааг шалгасан.

testInvalidHourThrows() – Эхлэх цаг 25, төгсөх цаг –1 гэх мэт буруу утга оруулахад алдаа гарч байгаа эсэхийг баталгаажуулсан.

testToStringContainsRoomAndDescription() – Уулзалтын хэвлэлт (toString()) нь өрөөний ID болон тайлбарыг агуулж байгааг шалгасан.

**PersonTest.java**

testAddMeeting() – Хүн уулзалтыг амжилттай нэмж чадаж байгаа эсэх.

testIsBusy() – Уулзалттай үед тухайн хүн завгүй гэж буцааж буй эсэх.

testVacationScheduling() – Амралт товлох функц зөв ажиллаж, тухайн өдөр уулзалттай байвал хориглож байгааг шалгасан.

**RoomTest.java**

testAddMeeting() – Өрөөнд анхны уулзалт амжилттай бүртгэгдэж буй эсэх.

testOverlappingNotAllowed() – Давхардсан уулзалт (нэг өрөөнд ижил цагт) үүсэх үед алдаа гарч буй эсэх.

testIsFreeAfterRemoval() – Уулзалт устгасны дараа өрөө сул болсныг шалгасан.

testPrintAgenda() – Өрөөний хуваарийн хэвлэл зөв ажиллаж буйг шалгасан.
**OrganizationTest.java**

testAddRoomAndPerson() – Байгууллагад хүн, өрөө амжилттай нэмэгдэж буй эсэх.

testFindPersonByName() – Хүнийг нэрээр нь зөв хайж буй эсэх.

testFindRoomByID() – Өрөөг ID-аар нь зөв хайж буй эсэх.

**Туршилтын үр дүн**
Бүх ангид нийт 30 орчим unit test хийгдсэн бөгөөд бүгд амжилттай (Passed) гарсан.
Тестүүдийн явцад 2 доголдол (null өрөө, coverage logic) илэрсэн .