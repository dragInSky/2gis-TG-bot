package tgbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    Parser parser = new Parser();

    @Test
    void findCode200Test() {
        String response = """
                {
                    "meta": {
                        "api_version": "3.0.426762",
                        "code": 200,
                        "issue_date": "20200506"
                    },
                    "result": {}
                }
                """;
        try {
            int code = parser.findCode(response);
            Assertions.assertEquals(200, code);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findCode400Test() {
        String response = """
                {
                    "meta": {
                        "code": 400,
                        "api_version": "dev",
                        "issue_date": "string",
                        "error": {}
                    }
                }
                """;
        try {
            int code = parser.findCode(response);
            Assertions.assertEquals(400, code);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findDurationTest() {
        String response = """
                {
                    "query": {},
                    "result": [
                        {
                            "algorithm": "с учётом пробок",
                            "begin_pedestrian_path": {},
                            "end_pedestrian_path": {},
                            "filter_road_types": [],
                            "id": "1805336109018823561",
                            "maneuvers": [],
                            "route_id": "...",
                            "total_distance": 15153,
                            "total_duration": 2204,
                            "type": "carrouting",
                            "ui_total_distance": {},
                            "ui_total_duration": "36 мин",
                            "waypoints": []
                        }
                    ],
                    "type": "result"
                }
                """;
        try {
            int duration = parser.findDuration(response);
            Assertions.assertEquals(2204, duration);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findStatusOK() {
        String response = """
                {
                    "query": {},
                    "result": [],
                    "type": "result",
                    "status": "OK"
                }
                """;
        try {
            String status = parser.findStatus(response);
            Assertions.assertEquals("OK", status);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findStatusROUTE_NOT_FOUND() {
        String response = """
                {
                    "query": {},
                    "result": [],
                    "type": "result",
                    "status": "ROUTE_NOT_FOUND"
                }
                """;
        try {
            String status = parser.findStatus(response);
            Assertions.assertEquals("ROUTE_NOT_FOUND", status);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findBuildingIdTest() {
        String response = """
                {
                    "meta": {},
                    "result": {
                        "context_rubrics": [],
                        "total": 1,
                        "search_attributes": {},
                        "items": [
                            {
                                "id": "141265769336625_f91d4H3777058262347790J0e8g28765",
                                "type": "branch",
                                "region_id": "123456",
                                "segment_id": "123456",
                            }
                        ]
                    }
                }
                """;
        try {
            String id = parser.findBuildingId(response);
            Assertions.assertEquals("141265769336625_f91d4H3777058262347790J0e8g28765", id);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findCompanies() {
    }

    @Test
    void findRouteInformationTest() {
        String response = """
                {
                    "query": {},
                    "result": [
                        {
                            "algorithm": "с учётом пробок",
                            "begin_pedestrian_path": {},
                            "end_pedestrian_path": {},
                            "filter_road_types": [],
                            "id": "1805336109018823561",
                            "maneuvers": [],
                            "route_id": "...",
                            "total_distance": 15153,
                            "total_duration": 2204,
                            "type": "carrouting",
                            "ui_total_distance": {
                                "unit": "км",
                                "value": "15"
                            },
                            "ui_total_duration": "47 мин",
                            "waypoints": []
                        }
                    ],
                    "type": "result"
                }
                """;
        try {
            String expected = "Расстояние маршрута: 15 км\nДлительность маршрута: 47 мин";
            String info = parser.findRouteInformation(response);
            Assertions.assertEquals(expected, info);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findCoordinates() {
    }

    @Test
    void findBuildingName() {
    }
}