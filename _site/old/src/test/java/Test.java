import java.io.FileNotFoundException;

public class Test {
    private static final String header_export = "GLOBALEVENTID    SQLDATE    MonthYear    Year    FractionDate    Actor1Code    Actor1Name    Actor1CountryCode    Actor1KnownGroupCode    Actor1EthnicCode    Actor1Religion1Code    Actor1Religion2Code    Actor1Type1Code    Actor1Type2Code    Actor1Type3Code    Actor2Code    Actor2Name    Actor2CountryCode    Actor2KnownGroupCode    Actor2EthnicCode    Actor2Religion1Code    Actor2Religion2Code    Actor2Type1Code    Actor2Type2Code    Actor2Type3Code    IsRootEvent    EventCode    EventBaseCode    EventRootCode    QuadClass    GoldsteinScale    NumMentions    NumSources    NumArticles    AvgTone    Actor1Geo_Type    Actor1Geo_FullName    Actor1Geo_CountryCode    Actor1Geo_ADM1Code    Actor1Geo_ADM2Code    Actor1Geo_Lat    Actor1Geo_Long    Actor1Geo_FeatureID    Actor2Geo_Type    Actor2Geo_FullName    Actor2Geo_CountryCode    Actor2Geo_ADM1Code    Actor2Geo_ADM2Code    Actor2Geo_Lat    Actor2Geo_Long    Actor2Geo_FeatureID    ActionGeo_Type    ActionGeo_FullName    ActionGeo_CountryCode     ActionGeo_ADM1Code    ActionGeo_ADM2Code    ActionGeo_Lat    ActionGeo_Long    ActionGeo_FeatureID    DATEADDED    SOURCEURL";
    private static final String mapping_export = "streams/sgraphs/events.ttl";

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
//        String s = "830176411\t20190312\t201903\t2019\t2019.1973\t\t\t\t\t\t\t\t\t\t\tCHN\tCHINA\tCHN\t\t\t\t\t\t\t\t1\t031\t031\t03\t1\t5.2\t4\t2\t4\t-0.15527950310559\t0\t\t\t\t\t\t\t\t4\tCarhaix, Bretagne, France\tFR\tFRA2\t16257\t48.2786\t-3.56723\t-1416739\t4\tCarhaix, Bretagne, France\tFR\tFRA2\t16257\t48.2786\t-3.56723\t-1416739\t20190312143000\thttps://www.investing.com/news/stock-market-news/sodiaal-takes-over-french-baby-formula-plant-from-chinas-synutra-1804921\n" +
//                "830176412\t20190312\t201903\t2019\t2019.1973\t\t\t\t\t\t\t\t\t\t\tCHN\tXINJIANG\tCHN\t\t\t\t\t\t\t\t1\t046\t046\t04\t1\t7.0\t10\t1\t10\t0\t0\t\t\t\t\t\t\t\t4\tXinjiang, Jiangxi, China\tCH\tCH03\t13167\t27.7481\t116.274\t11065838\t4\tXinjiang, Jiangxi, China\tCH\tCH03\t13167\t27.7481\t116.274\t11065838\t20190312143000\thttps://www.dw.com/en/qa-with-dolkun-isa-president-of-the-world-uyghur-congress/av-47870479\n";
//        GDELTWebSocketHandler handler;
//        Service service2 = Service.ignite().port(8080).threadPool(10);
//        service2.webSocket("/events", handler = new GDELTWebSocketHandler(header_export, mapping_export, '\t'));
//        service2.init();
//        Thread.sleep(4000);
//
//        handler.bindInputStream("GDELTStream", new ByteArrayInputStream(s.getBytes()));

        String h ="GKGRECORDID\tDATE\tSourceCollectionIdentifier\tSourceCommonName\tDocumentIdentifier\tCounts\tV2Counts\tThemes\tV2Themes\tLocations\tV2Locations\tPersons\tV2Persons\tOrganizations\tV2Organizations\tV2Tone\tDates\tGCAM\tSharingImage\tRelatedImages\tSocialImageEmbeds\tSocialVideoEmbeds\tQuotations\tAllNames\tAmounts\tTranslationInfo\tExtras";
            String s ="20190319131500-90\t20190319131500\t1\tnorthafricapost.com\thttp://northafricapost.com/29042-india-africa-partnership-morocco-takes-part-in-14th-conclave-of-new-delhi.html\t\t\tTAX_ETHNICITY;TAX_ETHNICITY_MOROCCAN;TAX_FNCACT;TAX_FNCACT_MINISTER;LEADER;EPU_ECONOMY_HISTORIC;USPEC_POLICY1;EPU_ECONOMY;TAX_ETHNICITY_INDIAN;TAX_FNCACT_LEADERS;UNGP_FORESTS_RIVERS_OCEANS;WB_137_WATER;WATER_SECURITY;UNGP_CLEAN_WATER_SANITATION;MANMADE_DISASTER_IMPLIED;WB_165_AIR_TRANSPORT;WB_135_TRANSPORT;WB_164_MODES_OF_TRANSPORT;WB_698_TRADE;GENERAL_GOVERNMENT;EPU_POLICY;EPU_POLICY_GOVERNMENT;WB_2433_CONFLICT_AND_VIOLENCE;WB_2432_FRAGILITY_CONFLICT_AND_VIOLENCE;SOC_INNOVATION;AGRICULTURE;CRISISLEX_C07_SAFETY;FOOD_SECURITY;WB_199_FOOD_SECURITY;WB_435_AGRICULTURE_AND_FOOD_SECURITY;WB_1967_AGRICULTURAL_RISK_AND_SECURITY;UNGP_AFFORDABLE_NUTRITIOUS_FOOD;WB_2601_TRADE_LINKAGES_SPILLOVERS_AND_CONNECTIVITY;WB_772_TRADE_FACILITATION_AND_LOGISTICS;WB_699_URBAN_DEVELOPMENT;WB_866_CONNECTIVITY_AND_LAGGING_REGIONS;WB_797_NATIONAL_URBAN_POLICIES;TAX_FNCACT_BUSINESSMEN;MARITIME_INCIDENT;MARITIME;SOC_ECONCOOP;MEDIA_MSM;\tGENERAL_GOVERNMENT,1057;EPU_POLICY_GOVERNMENT,1057;WB_2433_CONFLICT_AND_VIOLENCE,1442;WB_2432_FRAGILITY_CONFLICT_AND_VIOLENCE,1442;TAX_FNCACT_LEADERS,422;EPU_ECONOMY_HISTORIC,51;EPU_ECONOMY_HISTORIC,357;EPU_ECONOMY_HISTORIC,825;EPU_ECONOMY_HISTORIC,1487;AGRICULTURE,1986;MANMADE_DISASTER_IMPLIED,842;WB_165_AIR_TRANSPORT,842;WB_135_TRANSPORT,842;WB_164_MODES_OF_TRANSPORT,842;SOC_ECONCOOP,2474;MARITIME_INCIDENT,2421;MARITIME,2421;CRISISLEX_C07_SAFETY,2000;MEDIA_MSM,2585;WB_2601_TRADE_LINKAGES_SPILLOVERS_AND_CONNECTIVITY,2022;WB_772_TRADE_FACILITATION_AND_LOGISTICS,2022;WB_699_URBAN_DEVELOPMENT,2022;WB_866_CONNECTIVITY_AND_LAGGING_REGIONS,2022;WB_797_NATIONAL_URBAN_POLICIES,2022;TAX_FNCACT_MINISTER,39;TAX_FNCACT_MINISTER,627;TAX_FNCACT_MINISTER,802;TAX_ETHNICITY_INDIAN,348;TAX_ETHNICITY_INDIAN,404;TAX_ETHNICITY_INDIAN,793;TAX_ETHNICITY_INDIAN,1046;TAX_ETHNICITY_INDIAN,1455;TAX_ETHNICITY_INDIAN,1934;TAX_ETHNICITY_INDIAN,2193;TAX_ETHNICITY_INDIAN,2251;TAX_ETHNICITY_INDIAN,2346;TAX_FNCACT_BUSINESSMEN,2205;USPEC_POLICY1,90;USPEC_POLICY1,1360;USPEC_POLICY1,1630;EPU_ECONOMY,90;EPU_ECONOMY,1360;EPU_ECONOMY,1630;FOOD_SECURITY,2000;WB_199_FOOD_SECURITY,2000;WB_435_AGRICULTURE_AND_FOOD_SECURITY,2000;WB_1967_AGRICULTURAL_RISK_AND_SECURITY,2000;UNGP_AFFORDABLE_NUTRITIOUS_FOOD,2000;TAX_ETHNICITY_MOROCCAN,10;LEADER,42;LEADER,805;WATER_SECURITY,768;UNGP_CLEAN_WATER_SANITATION,768;WB_698_TRADE,72;WB_698_TRADE,978;WB_698_TRADE,1133;WB_698_TRADE,1432;WB_698_TRADE,2270;WB_137_WATER,768;SOC_INNOVATION,1889;UNGP_FORESTS_RIVERS_OCEANS,570;\t1#India#IN#IN#20#77#IN;4#New Delhi, Delhi, India#IN#IN07#28.6#77.2#-2106102\t1#India#IN#IN##20#77#IN#226;1#India#IN#IN##20#77#IN#597;1#India#IN#IN##20#77#IN#911;1#India#IN#IN##20#77#IN#965;1#India#IN#IN##20#77#IN#1280;1#India#IN#IN##20#77#IN#1528;1#India#IN#IN##20#77#IN#1580;1#India#IN#IN##20#77#IN#1834;1#India#IN#IN##20#77#IN#2488;4#New Delhi, Delhi, India#IN#IN07#17911#28.6#77.2#-2106102#219;4#New Delhi, Delhi, India#IN#IN07#17911#28.6#77.2#-2106102#1827\tmoulay hafid elalamy;suresh prabhakar prabhu;prabhakar prabhu\tMoulay Hafid Elalamy,113;Suresh Prabhakar Prabhu,868;Prabhakar Prabhu,868;Prabhakar Prabhu,1229\texim bank;africa project partnership;indian ministry of commerce;confederation of indian industry\tExim Bank,373;Indian Ministry Of Commerce,1476;Confederation Of Indian Industry,357\t0.697674418604651,1.86046511627907,1.16279069767442,3.02325581395349,16.046511627907,0,380\t1#0#0#2005#2406\twc:380,c1.2:12,c12.1:13,c12.10:39,c12.12:4,c12.13:15,c12.14:22,c12.3:5,c12.4:5,c12.5:5,c12.7:9,c12.8:18,c12.9:25,c13.10:2,c13.12:1,c13.9:1,c14.1:22,c14.10:7,c14.11:36,c14.2:18,c14.3:12,c14.4:2,c14.5:23,c14.7:4,c14.9:4,c15.102:1,c15.11:1,c15.110:1,c15.112:1,c15.147:1,c15.154:1,c15.212:1,c15.219:1,c15.233:2,c15.251:1,c15.261:1,c15.4:1,c15.46:1,c15.57:1,c15.62:1,c15.80:1,c16.1:2,c16.100:5,c16.101:4,c16.105:4,c16.106:11,c16.109:20,c16.11:5,c16.110:32,c16.113:2,c16.114:18,c16.116:11,c16.117:5,c16.118:22,c16.12:25,c16.120:16,c16.121:31,c16.122:4,c16.125:19,c16.126:14,c16.127:20,c16.128:5,c16.129:34,c16.13:2,c16.130:2,c16.131:11,c16.134:29,c16.138:9,c16.139:11,c16.14:2,c16.140:2,c16.141:2,c16.145:17,c16.146:15,c16.147:4,c16.152:4,c16.153:10,c16.157:9,c16.159:27,c16.16:8,c16.161:21,c16.162:11,c16.163:22,c16.164:5,c16.165:2,c16.19:10,c16.2:19,c16.21:1,c16.22:3,c16.24:1,c16.26:32,c16.27:2,c16.28:3,c16.29:3,c16.3:5,c16.30:3,c16.31:22,c16.32:4,c16.33:21,c16.34:2,c16.35:20,c16.37:35,c16.38:7,c16.4:26,c16.41:11,c16.43:3,c16.45:13,c16.46:6,c16.47:69,c16.48:13,c16.49:4,c16.5:2,c16.50:5,c16.51:2,c16.52:42,c16.55:1,c16.56:20,c16.57:167,c16.58:27,c16.6:30,c16.60:12,c16.62:13,c16.63:10,c16.64:1,c16.65:2,c16.66:4,c16.68:9,c16.69:11,c16.7:4,c16.70:19,c16.71:9,c16.72:4,c16.73:3,c16.74:7,c16.75:12,c16.76:3,c16.77:1,c16.78:1,c16.81:2,c16.83:2,c16.84:16,c16.85:2,c16.87:30,c16.88:26,c16.89:8,c16.90:11,c16.91:5,c16.92:19,c16.93:5,c16.94:30,c16.95:15,c16.96:21,c16.98:31,c16.99:1,c17.1:76,c17.10:39,c17.11:45,c17.12:10,c17.13:2,c17.14:4,c17.15:42,c17.16:10,c17.17:1,c17.18:6,c17.19:10,c17.2:13,c17.20:7,c17.22:9,c17.23:8,c17.24:33,c17.25:11,c17.27:26,c17.28:1,c17.29:7,c17.3:4,c17.30:4,c17.31:22,c17.32:16,c17.33:10,c17.34:5,c17.35:2,c17.36:10,c17.37:6,c17.38:1,c17.39:11,c17.4:75,c17.40:3,c17.41:14,c17.42:15,c17.43:9,c17.5:88,c17.6:4,c17.7:38,c17.8:43,c17.9:7,c18.13:1,c18.139:2,c18.165:1,c18.180:10,c18.193:5,c18.272:1,c18.333:1,c18.342:3,c18.35:2,c18.352:1,c18.52:1,c18.93:1,c18.94:1,c2.1:12,c2.101:3,c2.102:15,c2.103:1,c2.104:41,c2.106:14,c2.109:1,c2.11:5,c2.110:35,c2.111:2,c2.112:2,c2.114:6,c2.116:1,c2.119:133,c2.12:17,c2.120:1,c2.121:23,c2.122:2,c2.125:9,c2.126:14,c2.127:35,c2.128:15,c2.129:56,c2.130:1,c2.131:2,c2.132:9,c2.133:15,c2.134:1,c2.135:1,c2.136:8,c2.137:1,c2.139:4,c2.14:36,c2.141:5,c2.143:39,c2.144:2,c2.146:2,c2.147:77,c2.148:12,c2.15:20,c2.151:1,c2.152:1,c2.153:3,c2.154:9,c2.155:43,c2.156:8,c2.157:40,c2.158:21,c2.159:3,c2.16:1,c2.160:14,c2.162:2,c2.165:3,c2.166:5,c2.167:1,c2.169:1,c2.17:2,c2.170:2,c2.171:9,c2.172:1,c2.173:3,c2.174:3,c2.175:1,c2.176:1,c2.177:22,c2.179:5,c2.18:11,c2.180:11,c2.181:13,c2.183:13,c2.185:74,c2.186:2,c2.187:13,c2.19:3,c2.191:4,c2.192:3,c2.193:25,c2.194:2,c2.195:45,c2.196:4,c2.197:7,c2.198:22,c2.199:5,c2.200:7,c2.203:6,c2.204:22,c2.205:6,c2.206:2,c2.207:4,c2.209:8,c2.21:2,c2.210:30,c2.211:1,c2.213:2,c2.214:17,c2.215:3,c2.216:1,c2.217:8,c2.218:1,c2.220:12,c2.221:18,c2.222:2,c2.223:24,c2.224:4,c2.225:1,c2.226:8,c2.23:13,c2.25:24,c2.26:12,c2.27:12,c2.28:6,c2.30:19,c2.31:14,c2.32:2,c2.33:2,c2.34:19,c2.35:1,c2.36:1,c2.37:1,c2.39:43,c2.42:1,c2.43:1,c2.44:31,c2.45:69,c2.46:28,c2.47:1,c2.48:12,c2.50:7,c2.52:29,c2.54:33,c2.55:2,c2.56:2,c2.57:5,c2.58:31,c2.59:1,c2.60:4,c2.62:11,c2.63:1,c2.64:14,c2.65:1,c2.66:2,c2.67:1,c2.68:1,c2.71:1,c2.72:1,c2.73:2,c2.74:2,c2.75:63,c2.76:240,c2.77:34,c2.78:46,c2.79:1,c2.8:2,c2.80:46,c2.81:6,c2.82:8,c2.83:5,c2.84:1,c2.86:5,c2.87:10,c2.88:6,c2.89:7,c2.9:4,c2.90:1,c2.93:9,c2.95:57,c2.96:1,c2.97:1,c2.98:27,c2.99:2,c25.1:2,c25.7:1,c3.1:7,c3.2:27,c35.1:6,c35.11:5,c35.12:3,c35.14:5,c35.15:7,c35.2:2,c35.20:8,c35.25:6,c35.3:3,c35.31:29,c35.32:11,c35.33:14,c35.4:2,c35.5:5,c35.6:1,c35.7:2,c39.12:1,c39.14:1,c39.17:4,c39.19:5,c39.2:4,c39.3:13,c39.30:1,c39.32:1,c39.36:3,c39.37:15,c39.38:1,c39.39:2,c39.4:9,c39.40:1,c39.41:8,c39.5:6,c39.6:2,c39.7:1,c4.12:1,c4.16:2,c4.20:1,c4.23:11,c4.9:2,c40.3:1,c40.7:1,c40.8:1,c41.1:25,c5.10:29,c5.11:11,c5.12:58,c5.13:3,c5.16:1,c5.17:4,c5.19:2,c5.21:2,c5.22:1,c5.23:5,c5.24:4,c5.25:2,c5.26:4,c5.27:3,c5.28:4,c5.29:2,c5.3:4,c5.30:25,c5.33:1,c5.34:1,c5.35:11,c5.36:13,c5.37:9,c5.38:8,c5.4:20,c5.40:20,c5.42:1,c5.43:11,c5.44:1,c5.45:6,c5.46:73,c5.47:4,c5.48:1,c5.49:18,c5.5:1,c5.50:24,c5.51:14,c5.52:25,c5.53:35,c5.54:8,c5.55:1,c5.56:1,c5.6:14,c5.60:2,c5.61:10,c5.62:141,c5.7:11,c5.8:37,c5.9:13,c6.1:1,c6.2:2,c6.4:4,c6.5:8,c7.1:10,c7.2:17,c8.10:1,c8.11:2,c8.14:1,c8.17:4,c8.18:1,c8.2:1,c8.20:3,c8.22:2,c8.23:7,c8.24:1,c8.25:1,c8.28:2,c8.36:1,c8.37:1,c8.38:16,c8.4:11,c8.40:2,c8.42:9,c8.43:6,c8.8:2,c9.1:13,c9.10:4,c9.1002:1,c9.1003:1,c9.1004:1,c9.1014:2,c9.1023:1,c9.1024:1,c9.1030:1,c9.1038:6,c9.1039:4,c9.104:1,c9.1040:1,c9.1041:3,c9.105:2,c9.109:2,c9.111:2,c9.118:1,c9.119:1,c9.122:2,c9.123:1,c9.125:2,c9.127:3,c9.128:13,c9.129:4,c9.13:1,c9.130:2,c9.134:2,c9.135:4,c9.141:6,c9.142:2,c9.143:2,c9.145:2,c9.15:2,c9.151:1,c9.157:1,c9.158:5,c9.159:2,c9.160:2,c9.161:1,c9.162:2,c9.164:2,c9.165:1,c9.166:5,c9.168:4,c9.174:1,c9.177:4,c9.178:1,c9.182:2,c9.184:3,c9.186:8,c9.187:1,c9.188:5,c9.19:4,c9.192:2,c9.193:3,c9.196:1,c9.197:1,c9.198:3,c9.2:1,c9.200:1,c9.201:7,c9.203:4,c9.204:1,c9.205:1,c9.206:2,c9.207:2,c9.208:1,c9.209:1,c9.211:1,c9.212:3,c9.215:1,c9.217:3,c9.219:1,c9.221:1,c9.223:1,c9.224:3,c9.227:1,c9.233:3,c9.235:3,c9.236:1,c9.237:2,c9.238:4,c9.241:1,c9.246:1,c9.247:1,c9.25:2,c9.250:3,c9.253:1,c9.260:2,c9.262:1,c9.263:1,c9.266:2,c9.27:1,c9.270:1,c9.274:1,c9.277:1,c9.28:1,c9.280:2,c9.284:2,c9.286:1,c9.288:3,c9.290:1,c9.291:1,c9.292:1,c9.294:1,c9.296:1,c9.3:12,c9.30:1,c9.300:1,c9.301:2,c9.302:1,c9.304:1,c9.305:2,c9.306:2,c9.308:2,c9.310:1,c9.312:1,c9.316:1,c9.317:4,c9.320:1,c9.322:1,c9.326:1,c9.328:1,c9.33:10,c9.331:1,c9.332:1,c9.335:5,c9.339:1,c9.34:5,c9.347:1,c9.349:1,c9.35:11,c9.352:2,c9.353:1,c9.358:2,c9.368:1,c9.37:6,c9.370:1,c9.382:1,c9.383:2,c9.384:1,c9.39:11,c9.40:2,c9.409:1,c9.411:1,c9.42:1,c9.430:2,c9.44:5,c9.440:1,c9.442:1,c9.447:1,c9.454:9,c9.458:1,c9.459:1,c9.46:3,c9.465:1,c9.468:3,c9.47:2,c9.474:1,c9.476:2,c9.477:2,c9.478:1,c9.479:2,c9.48:6,c9.480:2,c9.488:3,c9.489:2,c9.49:1,c9.491:1,c9.494:1,c9.496:1,c9.497:2,c9.498:3,c9.5:1,c9.502:3,c9.507:1,c9.511:6,c9.513:2,c9.517:2,c9.518:2,c9.519:2,c9.521:3,c9.534:1,c9.537:1,c9.539:2,c9.54:2,c9.549:2,c9.551:3,c9.554:1,c9.556:3,c9.557:1,c9.559:3,c9.560:5,c9.562:2,c9.564:1,c9.566:4,c9.571:1,c9.574:1,c9.575:3,c9.576:4,c9.579:11,c9.589:1,c9.59:2,c9.598:1,c9.6:1,c9.602:1,c9.606:2,c9.61:3,c9.611:1,c9.613:2,c9.616:1,c9.617:2,c9.618:3,c9.619:4,c9.62:2,c9.624:3,c9.625:1,c9.626:2,c9.627:1,c9.629:1,c9.630:3,c9.631:1,c9.632:1,c9.635:3,c9.638:2,c9.640:2,c9.641:4,c9.642:4,c9.643:2,c9.646:2,c9.648:4,c9.649:2,c9.653:25,c9.654:3,c9.655:1,c9.658:2,c9.659:4,c9.66:2,c9.660:4,c9.663:1,c9.664:3,c9.665:5,c9.667:2,c9.668:1,c9.669:7,c9.670:5,c9.672:1,c9.676:4,c9.677:1,c9.678:1,c9.679:1,c9.682:1,c9.685:1,c9.686:6,c9.687:4,c9.688:1,c9.690:1,c9.692:1,c9.693:1,c9.694:1,c9.695:1,c9.699:1,c9.7:1,c9.70:4,c9.701:5,c9.704:2,c9.705:1,c9.710:6,c9.711:1,c9.713:1,c9.714:4,c9.717:2,c9.718:3,c9.719:1,c9.720:8,c9.721:2,c9.722:6,c9.724:7,c9.726:16,c9.730:11,c9.735:8,c9.736:4,c9.737:4,c9.740:10,c9.741:4,c9.744:3,c9.745:2,c9.746:1,c9.748:8,c9.75:1,c9.751:1,c9.752:3,c9.754:1,c9.757:1,c9.759:3,c9.76:7,c9.762:13,c9.765:1,c9.766:7,c9.767:13,c9.771:1,c9.775:3,c9.776:3,c9.778:1,c9.785:1,c9.788:3,c9.789:3,c9.79:1,c9.790:1,c9.792:1,c9.795:1,c9.8:3,c9.802:2,c9.806:1,c9.809:1,c9.810:9,c9.812:1,c9.816:4,c9.819:4,c9.821:1,c9.826:7,c9.828:1,c9.829:6,c9.83:3,c9.830:1,c9.832:7,c9.833:1,c9.834:3,c9.836:1,c9.837:1,c9.839:1,c9.843:3,c9.845:2,c9.846:1,c9.847:1,c9.849:1,c9.853:3,c9.858:1,c9.86:1,c9.860:3,c9.861:1,c9.863:1,c9.864:12,c9.865:1,c9.866:2,c9.868:12,c9.87:1,c9.871:1,c9.874:1,c9.877:4,c9.882:4,c9.883:1,c9.884:1,c9.890:3,c9.896:2,c9.899:1,c9.90:1,c9.900:1,c9.902:1,c9.903:3,c9.908:1,c9.911:4,c9.912:2,c9.913:1,c9.923:1,c9.924:1,c9.926:5,c9.932:3,c9.935:7,c9.938:2,c9.941:1,c9.945:2,c9.954:1,c9.955:4,c9.96:10,c9.964:1,c9.966:2,c9.969:2,c9.972:4,c9.978:2,c9.98:2,c9.980:1,c9.981:1,c9.984:3,c9.985:1,c9.990:1,c9.993:1,c9.994:1,c9.995:2,v10.1:0.252668489767114,v10.2:0.224228922207736,v11.1:0.156853860215054,v19.1:6.14454545454545,v19.2:5.08818181818182,v19.3:5.48,v19.4:6.17318181818182,v19.5:5.09818181818182,v19.6:5.65318181818182,v19.7:6.15681818181818,v19.8:5.16181818181818,v19.9:5.38818181818182,v20.1:0.405,v20.11:0.664571428571429,v20.13:0.448952380952381,v20.14:-0.437,v20.15:0.357763157894737,v20.16:-0.29675,v20.3:0.529666666666667,v20.5:0.64725,v20.7:0.6815,v20.9:0.664571428571429,v21.1:5.54776357827477,v26.1:1.00909090909091\t\t\t\t\t\tDigital Economy,102;Moulay Hafid Elalamy,128;India-Africa Project Partnership,206;New Delhi,244;Indian Industry,393;Exim Bank,415;Indian Minister,868;Civil Aviation,913;Suresh Prabhakar Prabhu,942;Indian Government,1130;Indian Ministry,1558;New Delhi,1933;North Africa,2670;Africa Post,2694\t37,African countries,231;57,pc of the African,590;37,pc without access,632;150000000000,dollars ,858;22,pc increase,954;2,regions,984;10,most dynamic economies,1510;400,African delegates,1851;300,Indian businessmen,1878;\t\t<PAGE_LINKS>http://northafricapost.com/author/northafricapost</PAGE_LINKS><PAGE_AUTHORS>North Africa Post</PAGE_AUTHORS><PAGE_PRECISEPUBTIMESTAMP>20190319124600</PAGE_PRECISEPUBTIMESTAMP>\n";
        System.out.println(h.split("\t").length);
        System.out.println(s.split("\t").length);
    }
}