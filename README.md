# Formedix App: Currency Rate API

The Currency Rate API provides endpoints for managing currency rate transactions.

## Configuration

To configure the Currency Rate Controller, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE.
3. Configure the application yaml located in `src/main/resources/application.yaml` according to your
   environment.

## Initial Data

By default, the application loads the `src/main/resources/templates/csv/default-currency-rates.csv` file during startup.
This file contains the initial
currency rate data. If you want to override the default data, you can upload a new CSV file using the provided API
endpoint.

## Building

To build the application using Maven, follow these steps:

1. Open a command-line interface.
2. Navigate to the root directory of the project.
3. Run the following command to build the application:

   ```shell
   mvn clean install
   ```

   This will compile the source code, run tests, and package the application into an executable JAR file.

## Running

To run the application using Maven, follow these steps:

1. Ensure that the application has been built successfully using the previous "Building" step.
2. In the command-line interface, navigate to the root directory of the project.
3. Run the following command to start the application:

   ```shell
   mvn spring-boot:run
   ```

   This will start the application, and you will be able to access the API endpoints from a web browser or API testing
   tool.

4. Once the application is running, you can access the API endpoints by sending HTTP requests to the appropriate URLs,
   as described in the "API Endpoints" section of this README.

## API Endpoints

### Get Currency Rates by Date

Retrieves the reference rate data for a given date for all available currencies.

- **Endpoint:** `GET /formedix/currency-rates/v1/{date}`
- **Description:** Retrieve the reference rate data for a given date for all available currencies.
- **Tags:** Currency Rate

### Convert Currency

Converts an amount from the source currency to the target currency on a specific date.

- **Endpoint:** `GET /formedix/currency-rates/v1/convert`
- **Description:** Convert an amount from the source currency to the target currency on a specific date.
- **Tags:** Currency Rate

### Get Highest Exchange Rate

Retrieve the highest reference exchange rate achieved by a currency within a specified period.

- **Endpoint:** `GET /formedix/currency-rates/v1/highest-rate`
- **Description:** Retrieve the highest reference exchange rate achieved by a currency within a specified period.
- **Tags:** Currency Rate

### Get Average Exchange Rate

Calculate the average reference exchange rate of a currency within a specified period.

- **Endpoint:** `GET /formedix/currency-rates/v1/average-rate`
- **Description:** Calculate the average reference exchange rate of a currency within a specified period.
- **Tags:** Currency Rate

### Upload Currency Rate CSV

Uploads and stores a CSV file containing currency rate data. This will override the default currency rate data.

- **Endpoint:** `POST /formedix/csv/v1/upload`
- **Description:** Upload and store the CSV file containing currency rate data. This will override the default currency
  rate data.
- **Tags:** CSV File Management

### Get Current Currency Rate CSV

Retrieves the currently uploaded CSV file containing currency rate data.

- **Endpoint:** `GET /formedix/csv/v1/current-upload`
- **Description:** Retrieves the currently uploaded CSV file containing currency rate data.
- **Tags:** CSV File Management

## Request Parameters

The following request parameters are used in the API endpoints:

- `{date}` (

path variable): The date for retrieving currency rates.

- `sourceCurrency` (query parameter): The source currency for currency conversion.
- `targetCurrency` (query parameter): The target currency for currency conversion.
- `amount` (query parameter): The amount to convert.
- `startDate` (query parameter): The start date for retrieving the highest/average exchange rate.
- `endDate` (query parameter): The end date for retrieving the highest/average exchange rate.
- `currency` (query parameter): The currency for retrieving the highest/average exchange rate.

## Response

The API endpoints return various response types based on the operation:

- `ResponseEntity<GetCurrencyRateDto>`: Response containing the currency rates for a specific date.
- `ResponseEntity<ConvertCurrencyDto>`: Response containing the converted amount from one currency to another.
- `ResponseEntity<HighestExchangeRateDto>`: Response containing the highest exchange rate achieved by a currency within
  a specified period.
- `ResponseEntity<AverageExchangeRateDto>`: Response containing the average exchange rate of a currency within a
  specified period.
- `ResponseEntity<CurrencyRates>`: Response containing the uploaded currency rate CSV data.

## Error Handling

The Currency Rate Controller handles the following error scenarios:

- `CurrencyRateNotFoundException`: Returns a 404 Not Found response with an appropriate error message.
- `FormedixException`: Returns a 500 Internal Server Error response with an error message.
- `CsvParsingException`: Returns a 400 Bad Request response with an error message.
- `MethodArgumentNotValidException`: Returns a 400 Bad Request response with validation error messages.
- Other Exceptions: Returns a 500 Internal Server Error response with an error message.