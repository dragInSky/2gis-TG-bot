package tgbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

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
            String code = parser.findCode(response);
            Assertions.assertEquals("200", code);
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
                        "error": {
                            "type": "paramIsEmpty",
                            "message": "Param 'lon' mustn`t be empty"
                        }
                    }
                }
                """;
        try {
            String code = parser.findCode(response);
            Assertions.assertEquals("Param 'lon' mustn`t be empty", code);
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
        String response = """
                {
                    "meta": {},
                    "result": {
                    "items": [
                        {
                            "address_comment": "1 этаж",
                            "address_name": "8 Марта, 51",
                            "id": "1267166676656141",
                            "name": "Simple coffee, кофейня",
                            "type": "branch"
                        },
                        {
                            "address_comment": "2 этаж",
                            "address_name": "8 Марта, 51",
                            "id": "1267166676435645",
                            "name": "ThatBritish, парикмахерский салон",
                            "type": "branch"
                        },
                        {
                            "address_comment": "1 этаж",
                            "address_name": "8 Марта, 51",
                            "id": "70000001056023916",
                            "name": "Давыдов, ресторан-караоке",
                            "type": "branch"
                        },
                        {
                            "address_comment": "18 этаж",
                            "address_name": "8 Марта, 51",
                            "id": "70000001023444043",
                            "name": "Sky18, кофейня",
                            "type": "branch"
                        },
                        {
                            "address_comment": "1 этаж",
                            "address_name": "8 Марта, 51",
                            "ads": {},
                            "id": "70000001020490742",
                            "name": "TOPGUN, барбершоп",
                            "type": "branch"
                        },
                        {
                            "address_comment": "1 этаж",
                            "address_name": "8 Марта, 51",
                            "id": "70000001035454616",
                            "name": "freshFace, магазин для визажистов и бровистов",
                            "type": "branch"
                        },
                        {
                            "address_comment": "6 кабинет; 5 этаж",
                            "address_name": "8 Марта, 51",
                            "id": "70000001041696646",
                            "name": "Ингосстрах, Отдел урегулирования убытков",
                            "type": "branch"
                        },
                        {
                            "address_comment": "706 кабинет; 7 этаж",
                            "address_name": "8 Марта, 51",
                            "ads": {},
                            "id": "70000001033985942",
                            "name": "Kultura, стоматологическая клиника",
                            "type": "branch"
                        },
                        {
                            "address_comment": "2 этаж",
                            "address_name": "8 Марта, 51",
                            "ads": {},
                            "id": "70000001031626218",
                            "name": "Контур",
                            "type": "branch"
                        },
                        {
                            "address_comment": "1 этаж; рядом с эскалатором",
                            "address_name": "8 Марта, 51",
                            "id": "1267166676801036",
                            "name": "Хо-хо, студия цветов",
                            "type": "branch"
                        }
                    ],
                    "total": 55
                    }
                }
                """;
        try {
            String companies = parser.findCompanies(response);
            Assertions.assertEquals("""
                     - Simple coffee, кофейня
                     - ThatBritish, парикмахерский салон
                     - Давыдов, ресторан-караоке
                     - Sky18, кофейня
                     - TOPGUN, барбершоп
                     - freshFace, магазин для визажистов и бровистов
                     - Ингосстрах, Отдел урегулирования убытков
                     - Kultura, стоматологическая клиника
                     - Контур
                     - Хо-хо, студия цветов
                    """,
                    companies);
        } catch (Exception e) {
            fail();
        }
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
        String response = """
                {
                    "meta": {},
                    "result": {
                        "items": [
                            {
                                "address_name": "Садовническая, 25",
                                "full_name": "Москва, Садовническая, 25",
                                "id": "...",
                                "name": "Садовническая, 25",
                                "point": {
                                    "lat": 55.746397,
                                    "lon": 37.634369
                                },
                                "purpose_name": "Жилой дом с административными помещениями",
                                "type": "building"
                            }
                        ],
                        "total": 1
                    }
                }
                """;
        try {
            Coordinates expected = new Coordinates(55.746397, 37.634369);
            Coordinates coordinates = parser.findCoordinates(response);
            Assertions.assertEquals(expected, coordinates);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findBuildingName() {
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
                                "dates": {},
                                "adm_div": [],
                                "description": "string",
                                "is_routing_available": true,
                                "links": {},
                                "org": {},
                                "reviews": {},
                                "alias": "shintop_set_avtomarketov",
                                "timezone_offset": 420,
                                "has_apartments_info": true,
                                "floor_id": "141832714658709",
                                "context": {},
                                "flags": {},
                                "floor_plans": {},
                                "delivery": [],
                                "name": "Солнышко, кафе",
                                "point": "54.991984,82.901886",
                                "employees_org_count": "до 15",
                                "city_alias": "novosibirsk",
                                "address": {},
                                "floors": {},
                                "is_deleted": true,
                                "attribute_groups": [],
                                "itin": "1234567890",
                                "rubrics": [],
                                "marker_alt": 0,
                                "building_name": "someBuilding",
                            }
                        ]
                    }
                }
                """;
        try {
            String buildingName = parser.findBuildingName(response);
            Assertions.assertEquals("someBuilding", buildingName);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findAddressTest() {
        String response = """
                {
                    "meta": {},
                    "result": {
                        "items": [
                            {
                                "address_name": "Кремль, 1х",
                                "building_name": "Государственный Кремлёвский Дворец",
                                "full_name": "Москва, Государственный Кремлёвский Дворец",
                                "id": "...",
                                "name": "Государственный Кремлёвский Дворец",
                                "point": {},
                                "purpose_name": "Культурное учреждение",
                                "type": "building"
                            }
                        ],
                        "total": 1
                    }
                }
                """;
        try {
            String address = parser.findAddress(response);
            Assertions.assertEquals("Москва, Государственный Кремлёвский Дворец", address);
        } catch (Exception e) {
            fail();
        }
    }
}