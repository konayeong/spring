# Spring Boot Core 결산과제
## [상수도 요금 계산 CLI 프로그램]
### 요구사항


### Parsing
- OpenCSV
- 객체 자동 매핑, 쉬운 난이도, 낮은 유연성 
```java
public List<Account> accounts() {
    File targetFile = new File(fileProperties.getAccountPath());

    try(BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
        // CSVToBeanBuilder : Reader 기반으로 CSV 읽음
        return new CsvToBeanBuilder<Account>(br)
                .withType(Account.class)
                .build()
                 .parse();
    } catch (IOException e) {
         log.error("CSV Account 파싱 실패", e);
         throw new RuntimeException("account.csv 파싱 실패",e);
    }
}
```
- Apache Commons CSV
- [코드참고](src/main/java/com/example/core/common/parser/impl/CsvDataParser.java)

