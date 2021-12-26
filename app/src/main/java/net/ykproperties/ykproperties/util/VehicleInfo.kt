package net.ykproperties.ykproperties.util

object VehicleInfo {

    var carMakeModel = mutableMapOf(
        "Abarth" to listOf("124 Spider","500","500C","595","695","Grande Punto","Punto Evo"),
        "AC" to listOf("378 GT Zagato","Ace","Aceca","Cobra"),
        "Acura" to listOf("CL","CSX","EL","ILX","Integra","Legend","MDX","NSX","RDX","RL","RLX","RSX","SLX","TL","TLX","TSX","ZDX"),
        "Adler" to listOf("Diplomat","Trumpf Junior"),
        "AlfaRomeo" to listOf("105/115","145","146","147","155","156","159","164","166","1900","2600","33","4C","6","6C","75","8C Competizione","90","Alfasud","Alfetta","Arna","Brera","Disco Volante","Giulia","Giulietta","GT","GTA Coupe","GTV","MiTo","Montreal","RZ","Spider","Sprint","Stelvio","SZ"),
        "Alpina" to listOf("B10","B11","B12","B3","B4","B5","B6","B7","B8","B9","C1","C2","D10","D3","D4","D5","Roadster","XB7","XD3","XD4"),
        "Alpine" to listOf("A110","A310","A610","GTA"),
        "AMGeneral" to listOf("HMMWV (Humvee)"),
        "AMC" to listOf("Eagle","Hornet"),
        "Apal" to listOf("21541 Stalker"),
        "Ariel" to listOf("Atom"),
        "Aro" to listOf("10","24"),
        "Asia" to listOf("Retona","Rocsta","Topic","Towner"),
        "AstonMartin" to listOf("Bulldog","Cygnet","DB AR1","DB11","DB5","DB7","DB9","DBS","DBX","Lagonda","One-77","Rapide","Tickford Capri","V12 Vanquish","V12 Vantage","V12 Zagato","V8 Vantage","V8 Zagato","Virage"),
        "Auburn" to listOf("Speedster"),
        "Audi" to listOf("100","200","50","5000","80","90","920","A1","A2","A3","A4","A4 allroad","A5","A6","A6 allroad","A7","A8","Cabriolet","Coupe","e-tron","e-tron GT","e-tron S","e-tron S Sportback","e-tron Sportback","NSU RO 80","Q2","Q3","Q3 Sportback","Q4 e-tron","Q4 Sportback e-tron","Q5","Q5 Sportback","Q7","Q8","quattro","R8","R8 LMP","RS e-tron GT","RS Q3","RS Q3 Sportback","RS Q8","RS2","RS3","RS4","RS5","RS6","RS7","S1","S2","S3","S4","S5","S6","S7","S8","SQ2","SQ5","SQ5 Sportback","SQ7","SQ8","TT","TT RS","TTS","Typ R","V8"),
        "Aurus" to listOf("Senat"),
        "AustinHealey" to listOf("100","3000"),
        "Austin" to listOf("Allegro","Ambassador","FL2","FX4","Maestro","Maxi","Metro","Mini","Montego","Princess","Sprite"),
        "Autobianchi" to listOf("A 112"),
        "Avtokam" to listOf("2160","2163","3101"),
        "BAIC" to listOf("A1","BJ2020","BJ2026","BJ212","EU260","EU5","EX5","Jeep Cherokee 2500","Luba (XB624)"),
        "Bajaj" to listOf("Qute"),
        "BaltijasDzips" to listOf("BD-1322"),
        "Batmobile" to listOf("1989","2018"),
        "Bentley" to listOf("Arnage","Azure","Bentayga","Brooklands","Continental","Continental Flying Spur","Continental GT","Eight","Flying Spur","Mark VI","Mulsanne","R Type","S","T-Series","Turbo R"),
        "Bertone" to listOf("Freeclimber"),
        "Bilenkin" to listOf("Vintage"),
        "Bioauto" to listOf("evA-4"),
        "Bitter" to listOf("CD","Type 3"),
        "BMW" to listOf("02 (E10)","1er","1M","2000 C/CS","2er","2er Active Tourer","2er Grand Tourer","3/15","315","3200","321","326","327","340","3er","4er","501","502","503","507","5er","600","6er","700","7er","8er","E3","E9","i3","i4","i8","iX","iX3","M2","M3","M4","M5","M6","M8","New Class","X1","X2 Concept","X3","X3 M","X4","X4 M","X5","X5 M","X6","X6 M","X7","Z1","Z3","Z3 M","Z4","Z4 M","Z8"),
        "Borgward" to listOf("2000","Hansa 1100"),
        "Brabus" to listOf("7.3S","M V12","ML 63 Biturbo","SV12"),
        "Brilliance" to listOf("FRV (BS2)","H230","H530","M1 (BS6)","M2 (BS4)","M3 (BC3)","V3","V5"),
        "Bristol" to listOf("Blenheim","Blenheim Speedster","Fighter"),
        "Bronto" to listOf("Fora","Rysy"),
        "Bufori" to listOf("Geneva","La Joya"),
        "Bugatti" to listOf("Chiron","EB 110","EB 112","EB Veyron 16.4","Type 55"),
        "Buick" to listOf("Cascada","Century","Electra","Enclave","Encore","Envision","Estate Wagon","Excelle","GL8","GS","LaCrosse","LeSabre","Limited","Lucerne","Park Avenue","Rainer","Reatta","Regal","Rendezvous","Riviera","Roadmaster","Skyhawk","Skylark","Special","Super","Terraza","Verano","Wildcat"),
        "BYD" to listOf("E6","F0","F3","F5","F6","F8","Flyer","G3","G6","L3","M6","S6","Song EV","Tang","Yuan"),
        "Byvin" to listOf("BD132J (CoCo)","BD326J (Moca)"),
        "Cadillac" to listOf("Allante","ATS","ATS-V","BLS","Brougham","Catera","CT4","CT4-V","CT5","CT5-V","CT6","CTS","CTS-V","De Ville","DTS","Eldorado","ELR","Escalade","Fleetwood","LSE","Series 62","Seville","Sixty Special","SRX","STS","XLR","XT4","XT5","XT6","XTS"),
        "Callaway" to listOf("C12"),
        "Carbodies" to listOf("FX4"),
        "Caterham" to listOf("21","CSR","Seven"),
        "Chana" to listOf("Benni","SC6390"),
        "Changan" to listOf("Alsvin V7","Benni","Benni EC/EV","CM-8","CS35","CS35PLUS","CS55","CS75","CX20","Eado","Raeton","UNI-K","Z-Shine"),
        "ChangFeng" to listOf("Flying","SUV (CS6)"),
        "Changhe" to listOf("Ideal"),
        "Chery" to listOf("Amulet (A15)","Arrizo 3","Arrizo 7","B13","Bonus (A13)","Bonus 3 (E3/A19)","CrossEastar (B14)","E5","Fora (A21)","IndiS (S18D)","Karry","Kimo (A1)","M11 (A3)","Oriental Son (B11)","QQ6 (S21)","QQme","Sweet (QQ)","Tiggo (T11)","Tiggo 2","Tiggo 3","Tiggo 4","Tiggo 5","Tiggo 7","Tiggo 7 Pro","Tiggo 8","Tiggo 8 Pro","Very (A13)","Windcloud (A11)"),
        "CHERYEXEED" to listOf("TXL"),
        "Chevrolet" to listOf("3000-Series","Alero","Apache","Astra","Astro","Avalanche","Aveo","Bel Air","Beretta","Blazer","Blazer K5","Bolt","C-10","C/K","Camaro","Caprice","Captiva","Captiva Sport","Cavalier","Celebrity","Celta","Chevelle","Chevette","Citation","Classic","Cobalt","Colorado","Corsa","Corsica","Corvair","Corvette","Cruze","Cruze (HR)","CSV CR8","Deluxe","El Camino","Epica","Equinox","Evanda","Express","Fleetmaster","HHR","Impala","Kalos","Lacetti","Lanos","Lumina","Lumina APV","LUV D-MAX","Malibu","Master","Matiz","Metro","Monte Carlo","Monza","MW","Nexia","Niva","Nova","Nubira","Omega","Orlando","Prizm","Rezzo","S-10 Pickup","Sail","Silverado","Sonic","Spark","Special DeLuxe","Spin","SS","SSR","Starcraft","Suburban","Tacuma","Tahoe","Tavera","Tracker","TrailBlazer","Trans Sport","Traverse","Trax","Uplander","Van","Vectra","Venture","Viva","Volt","Zafira"),
        "Chrysler" to listOf("180","200","300","300 Letter Series","300C","300C SRT8","300M","Aspen","Cirrus","Concorde","Cordoba","Crossfire","Daytona","Dynasty","ES","Fifth Avenue","Imperial","Imperial Crown","Intrepid","Le Baron","LHS","Nassau","Neon","New Yorker","Newport","Pacifica","Prowler","PT Cruiser","Saratoga","Sebring","Six","Stratus","TC by Maserati","Town & Country","Viper","Vision","Voyager","Windsor"),
        "Citroen" to listOf("2 CV","AMI","AX","Berlingo","BX","C-Crosser","C-Elysee","C-Quatre","C-Triomphe","C-ZERO","C1","C2","C3","C3 Aircross","C3 Picasso","C4","C4 Aircross","C4 Cactus","C4 Picasso","C4 SpaceTourer","C5","C5 Aircross","C6","C8","CX","DS","DS3","DS4","DS5","Dyane","E-Mehari","Evasion","GS","Jumpy","LN","Nemo","Saxo","SM","Spacetourer","Traction Avant","Visa","Xantia","XM","Xsara","Xsara Picasso","ZX"),
        "Cizeta" to listOf("V16t"),
        "Coggiola" to listOf("T Rex"),
        "Cord" to listOf("L-29"),
        "Cupra" to listOf("Ateca","Formentor","Leon"),
        "Dacia" to listOf("1300","1310","1325","1410","Dokker","Duster","Jogger","Lodgy","Logan","Nova","Pick-Up","Sandero","Solenza","Spring","SuperNova"),
        "Dadi" to listOf("City Leading","Shuttle","Smoothing"),
        "Daewoo" to listOf("Alpheon","Arcadia","Damas","Espero","G2X","Gentra","Kalos","Korando","Lacetti","Lacetti Premiere","Lanos","Leganza","LeMans","Magnus","Matiz","Matiz Creative","Musso","Nexia","Nubira","Prince","Racer","Rezzo","Royale","Sens","Tacuma","Tico","Tosca","Winstorm"),
        "DAF" to listOf("46","66"),
        "Daihatsu" to listOf("Altis","Applause","Atrai","Be-go","Bee","Boon","Boon Luminas","Cast","Ceria","Charade","Charmant","Coo","Copen","Cuore","Delta Wagon","Esse","Extol","Fellow","Gran Move","Hijet","Hijet Caddie","Leeza","Materia","MAX","Mebius","Midget","Mira","Mira Cocoa","Mira e:S","Mira Gino","Move","Move Canbus","Move Conte","Move Latte","Naked","Opti","Pyzar","Rocky","Rugger","Sirion","Sonica","Storia","Taft","Tanto","Tanto Exe","Terios","Thor","Trevis","Wake","Wildcat","Xenia","YRV"),
        "Daimler" to listOf("DS420","Sovereign (XJ6)","SP250","X300","X308","X350","XJ40","XJS"),
        "Dallara" to listOf("Stradale"),
        "Datsun" to listOf("240Z","280Z","280ZX","720","Bluebird","Cherry","GO","GO+","Laurel","mi-DO","on-DO","Stanza","Sunny","Urvan","Violet"),
        "DeTomaso" to listOf("Bigua","Guara","Longchamp","Mangusta","Pantera","Vallelunga"),
        "DecoRides" to listOf("Zephyr"),
        "Delage" to listOf("D12","D6"),
        "DeLorean" to listOf("DMC-12"),
        "Derways" to listOf("Antelope","Aurora","Cowboy","DW Hower H3","Land Crown","Plutus","Saladin","Shuttle"),
        "DeSoto" to listOf("Custom","Firedome","Fireflite"),
        "DKW" to listOf("3=6"),
        "Dodge" to listOf("600","Aries","Avenger","Caliber","Caravan","Challenger","Charger","Charger Daytona","Colt","Custom Royal","D/W Series","D8","Dakota","Dart","Daytona","Diplomat","Durango","Dynasty","Intrepid","Journey","Lancer","Magnum","Mayfair","Monaco","Neon","Nitro","Omni","Polara","Raider","RAM","RAM Van","Ramcharger","Shadow","Spirit","Stealth","Stratus","Viper","WC"),
        "DongFeng" to listOf("370","580","A30","A9","AX7","H30 Cross","MPV","Oting","Rich","S30"),
        "Doninvest" to listOf("Assol","Kondor","Orion"),
        "Donkervoort" to listOf("D8","D8 Cosworth","D8 GT","D8 GTO","D8 Zetec"),
        "DS" to listOf("3","3 Crossback","4","5","7 Crossback"),
        "DWHower" to listOf("H3","H5"),
        "ECar" to listOf("GD04B"),
        "EagleCars" to listOf("SS"),
        "Eagle" to listOf("Premier","Summit","Talon","Vision","Vista"),
        "Ecomotors" to listOf("Estrima Biro"),
        "Excalibur" to listOf("Series IV","Series V"),
        "FAW" to listOf("Bestune T99","Besturn B30","Besturn B50","Besturn B70","Besturn X40","Besturn X80","City Golf","D60","Jinn","Oley","V2","V5","Vita"),
        "Ferrari" to listOf("208/308","250 GTO","296 GTB","328","348","360","400","412","456","458","488","512 BB","512 M","512 TR","550","575M","599","612","812 Superfast","California","Dino 206 GT","Dino 246 GT","Enzo","F12berlinetta","F355","F40","F430","F50","F8 Tributo","FF","FXX K","GTC4Lusso","LaFerrari","Mondial","Monza SP","Portofino","Roma","SF90 Stradale","Testarossa"),
        "Fiat" to listOf("124","124 Spider","124 Sport Spider","125","126","127","128","130","131","132","2300","238","500","500L","500X","508","600","900T","Albea","Argenta","Barchetta","Brava","Bravo","Cinquecento","Coupe","Croma","Doblo","Duna","Fiorino","Freemont","Fullback","Idea","Linea","Marea","Multipla","Palio","Panda","Punto","Qubo","Regata","Ritmo","Scudo","Sedici","Seicento","Siena","Stilo","Strada","Tempra","Tipo","Ulysse","Uno","X 1/9"),
        "Fisker" to listOf("Karma"),
        "Flanker" to listOf("F"),
        "Ford" to listOf("Aerostar","Aspire","B-MAX","Bronco","Bronco Sport","Bronco-II","C-MAX","Capri","Consul","Contour","Cortina","Cougar","Country Squire","Crown Victoria","Custom","Econoline","EcoSport","Edge","Escape","Escort","Escort (North America)","Everest","Excursion","Expedition","Explorer","Explorer Sport Trac","F-150","F-2","Fairlane","Fairmont","Falcon","Festiva","Fiesta","Fiesta ST","Five Hundred","Flex","Focus","Focus (North America)","Focus RS","Focus ST","Freda","Freestar","Freestyle","Fusion","Fusion (North America)","Galaxie","Galaxy","GPA","Granada","Granada (North America)","GT","GT40","Ikon","Ixion","KA","Kuga","Laser","LTD Country Squire","LTD Crown Victoria","M151","Mainline","Maverick","Model A","Model T","Mondeo","Mondeo ST","Mustang","Mustang Mach-E","Orion","Probe","Puma","Ranchero","Ranger","Ranger (North America)","S-MAX","Scorpio","Sierra","Spectron","Taunus","Taurus","Taurus X","Telstar","Territory","Thunderbird","Torino","Tourneo Connect","Tourneo Courier","Tourneo Custom","Transit Connect","V8","Windstar","Zephyr"),
        "Foton" to listOf("Midi","Sauvana","Tunland"),
        "FSO" to listOf("125p","126p","127p","132p","Lanos","Polonez","Warszawa"),
        "Fuqi" to listOf("6500 (Land King)"),
        "GAC" to listOf("GN8","GS5","Trumpchi GS8"),
        "GAZ" to listOf("12 ZIM","13 Chayka","14 Chayka","18","21 Volga","22 Volga","2308 Ataman","2330 Tigr","24 Volga","3102 Volga","31022 Volga","310221 Volga","31029 Volga","3103 Volga","3105 Volga","3110 Volga","31105 Volga","3111 Volga","46","61","64","67","69","A","M-20 Pobeda","M-72","M1","Volga Siber"),
        "Geely" to listOf("Atlas","Atlas Pro","Beauty Leopard","CK (Otaka)","Coolray","Emgrand 7","Emgrand EC7","Emgrand EC8","Emgrand GT","Emgrand X7","GC6","GC9","GS","Haoqing","LC (Panda) Cross","MK","MK Cross","MR","SC7","Tugella","TX4"),
        "Genesis" to listOf("G70","G80","G90","GV70","GV80"),
        "Geo" to listOf("Metro","Prizm","Spectrum","Storm","Tracker"),
        "GMC" to listOf("100","Acadia","Canyon","Envoy","Jimmy","Safari","Savana","Sierra","Sonoma","Suburban","Syclone","Terrain","Typhoon","Vandura","Yukon"),
        "Goggomobil" to listOf("T","TS"),
        "Gonow" to listOf("Troy"),
        "Gordon" to listOf("Roadster"),
        "GP" to listOf("Madison"),
        "GreatWall" to listOf("Coolbear","Florid","Hover","Hover H3","Hover H5","Hover H6","Hover M1 (Peri 4x4)","Hover M2","Hover M4","Hover Pi","Pegasus","Peri","Poer","Safe","Sailor","Sing RUV","Socool","Voleex C10 (Phenom)","Voleex C30","Wingle","Wingle 7"),
        "Hafei" to listOf("Brio","Princip","Saibao","Sigma","Simbo"),
        "Haima" to listOf("2","3","7","Family","Family F7","Freema","M3","S5"),
        "Hanomag" to listOf("Rekord","Typ 13"),
        "Haval" to listOf("F7","F7x","H2","H5","H6","H6 Coupe","H8","H9","Jolion"),
        "Hawtai" to listOf("B21","Boliger","Laville"),
        "Heinkel" to listOf("Typ 154"),
        "Hennessey" to listOf("Venom F5"),
        "Hindustan" to listOf("Ambassador","Contessa"),
        "HispanoSuiza" to listOf("K6"),
        "Holden" to listOf("Apollo","Astra","Barina","Calais","Caprice","Commodore","Cruze","Frontera","Jackaroo","Monaro","Rodeo","Statesman","UTE","Vectra","Zafira"),
        "Honda" to listOf("145","Accord","Acty","Airwave","Ascot","Ascot Innova","Avancier","Ballade","Beat","Brio","Capa","City","Civic","Civic Ferio","Civic Type R","Concerto","CR-V","CR-X","CR-Z","Crossroad","Crosstour","Domani","e","Edix","Element","Elysion","FCX Clarity","Fit","Fit Aria","Fit Shuttle","FR-V","Freed","Grace","Horizon","HR-V","Insight","Inspire","Integra","Integra SJ","Jade","Jazz","Lagreat","Legend","Life","Logo","MDX","Mobilio","Mobilio Spike","N-BOX","N-BOX Slash","N-One","N-WGN","N360","NSX","Odyssey","Odyssey (North America)","Orthia","Partner","Passport","Pilot","Prelude","Quint","Rafaga","Ridgeline","S-MX","S2000","S500","S600","S660","Saber","Shuttle","Stepwgn","Stream","Street","That'S","Today","Torneo","Vamos","Vezel","Vigor","Z","Zest"),
        "Horch" to listOf("830","853"),
        "HuangHai" to listOf("Antelope","Landscape","Plutus"),
        "Hudson" to listOf("Deluxe Eight","Super Six"),
        "Hummer" to listOf("H1","H2","H3"),
        "Hyundai" to listOf("Accent","Aslan","Atos","Avante","Azera","Bayon","Centennial","Click","Coupe","Creta","Dynasty","Elantra","Entourage","EON","Equus","Excel","Galloper","Genesis","Genesis Coupe","Getz","Grace","Grand Starex Urban","Grandeur","H200","i10","i20","i20 N","i30","i30 N","i40","IONIQ","IONIQ 5","ix20","ix35","ix55","Kona","Lantra","Lavita","Marcia","Matrix","Maxcruz","Palisade","Pony","Santa Fe","Santamo","Scoupe","Solaris","Sonata","Starex","Starex (H-1)","Staria","Stellar","Terracan","Tiburon","Trajet","Tucson","Tuscani","Veloster","Venue","Veracruz","Verna","XG"),
        "Infiniti" to listOf("EX","FX","G","I","J","JX","M","Q","Q30","Q40","Q50","Q60","Q70","QX30","QX4","QX50","QX55","QX56","QX60","QX70","QX80"),
        "Innocenti" to listOf("Elba","Mille","Mini"),
        "International" to listOf("Travelall"),
        "Invicta" to listOf("S1"),
        "IranKhodro" to listOf("Dena","Paykan","Runna","Sahra","Samand","Sarir","Soren"),
        "Isdera" to listOf("Commendatore 112i","Imperator 108i","Spyder"),
        "Isuzu" to listOf("117","Amigo","Ascender","Aska","Axiom","Bighorn","D-Max","Fargo","Fargo Filly","Florian","Gemini","Hombre","Impulse","KB","Midi","MU","MU-7","MU-X","Oasis","Piazza","Rodeo","Stylus","TF (Pickup)","Trooper","VehiCross","Wizard"),
        "IVECO" to listOf("Massif"),
        "Izh" to listOf("2125 Kombi","2126 Oda","21261 Fabula","2715","2717","27175","Moskvich-412"),
        "JAC" to listOf("iEV7L","iEV7S","iEVS4","J2 (Yueyue)","J3 (Tongyue,Tojoy)","J4 (Heyue A30)","J5 (Heyue)","J6 (Heyue RS)","J7","J7 (Binyue)","M1 (Refine)","M5","S1 (Rein)","S3","S5 (Eagle)","S7","T6"),
        "Jaguar" to listOf("E-Pace","E-type","F-Pace","F-Type","F-Type SVR","I-Pace","Mark 2","Mark IX","S-Type","X-Type","XE","XF","XFR","XJ","XJ220","XJR","XJS","XK","XKR"),
        "Jeep" to listOf("Cherokee","CJ","Commander","Compass","Gladiator","Grand Cherokee","Grand Cherokee SRT8","Grand Wagoneer","Liberty (North America)","Liberty (Patriot)","Renegade","Wrangler"),
        "Jensen" to listOf("Interceptor","S-V8"),
        "Jinbei" to listOf("Haise"),
        "JMC" to listOf("Baodian"),
        "Kanonir" to listOf("2317"),
        "Kia" to listOf("Avella","Cadenza","Capital","Carens","Carnival","Carstar","Cee'd","Cee'd GT","Cerato","Clarus","Concord","Elan","Enterprise","EV6","Forte","Joice","K3","K5","K7","K9","K900","Lotze","Magentis","Mentor","Mohave (Borrego)","Morning","Niro","Opirus","Optima","Picanto","Potentia","Pregio","Pride","Proceed","Quanlima","Quoris","Ray","Retona","Rio","Sedona","Seltos","Sephia","Shuma","Sorento","Soul","Soul EV","Spectra","Sportage","Stinger","Stonic","Telluride","Towner","Venga","Visto","X-Trek","XCeed"),
        "Koenigsegg" to listOf("Agera","CC8S","CCR","CCX","One:1","Regera"),
        "Kombat" to listOf("T98"),
        "KTM" to listOf("X-Bow"),
        "LadaVAZ" to listOf("1111 Oka","2101","2102","2103","2104","2105","2106","2107","2108","2109","21099","2110","2111","2112","2113","2114","2115","2120 Nadezhda","2121 (4x4)","2123","2129","2131 (4x4)","2328","2329","EL Lada","Granta","Kalina","Lada","Largus","Niva","Niva Legend","Priora","Revolution","Vesta","XRAY"),
        "Lamborghini" to listOf("  Select Model :","  350/400 GT","  Aventador","  Centanario","  Countach","  Countach LPI 800-4","  Diablo","  Egoista","  Espada","  Gallardo","  Huracán","  Islero","  Jalpa","  Jarama","  LM001","  LM002","  Miura","  Murcielago","  Reventon","  Sesto Elemento","  Sián","  Silhouette","  Urraco","  Urus Concept","  Veneno"),
        "Lancia" to listOf("A 112","Appia","Aurelia","Beta","Dedra","Delta","Flaminia","Flavia","Fulvia","Gamma","Hyena","Kappa","Lambda","Lybra","Monte Carlo","Musa","Phedra","Prisma","Rally 037","Stratos","Thema","Thesis","Trevi","Voyager","Y10","Ypsilon","Zeta"),
        "LandRover" to listOf("Defender","Discovery","Discovery Sport","Freelander","Range Rover","Range Rover Evoque","Range Rover Sport","Range Rover Velar","Series I","Series II","Series III"),
        "Landwind" to listOf("Fashion (CV9)","Forward","H9","X5","X6","X7"),
        "Lexus" to listOf("CT","ES","GS","GS F","GX","HS","IS","IS F","LC","LFA","LM","LS","LX","NX","RC","RC F","RX","SC","UX"),
        "LiebaoMotor" to listOf("Leopard"),
        "Lifan" to listOf("650 EV","Breez (520)","Cebrium (720)","Celliya (530)","Murman","Myway","Smily","Solano","X50","X60","X70"),
        "Ligier" to listOf("JS 51"),
        "Lincoln" to listOf("Aviator","Blackwood","Capri","Continental","Corsair","LS","Mark III","Mark IV","Mark LT","Mark VII","Mark VIII","MKC","MKS","MKT","MKX","MKZ","Nautilus","Navigator","Premiere","Town Car","Versailles","Zephyr"),
        "LiXiang" to listOf("One"),
        "Logem" to listOf("EC30"),
        "Lotus" to listOf("340R","Eclat","Elan","Elise","Elite","Esprit","Europa","Europa S","Evora","Excel","Exige"),
        "LTI" to listOf("TX"),
        "LUAZ" to listOf("1302 Volyny","967","969"),
        "Lucid" to listOf("Air Concept"),
        "Luxgen" to listOf("Luxgen5","Luxgen7 MPV","Luxgen7 SUV","U6 Turbo","U7 Turbo"),
        "Mahindra" to listOf("Armada","Bolero","CJ-3","CL","Commander","Marshal","MM","NC 640 DP","Scorpio","Verito","Voyager","Xylo"),
        "Marcos" to listOf("GTS","LM 400","LM 500","Mantis","Marcasite"),
        "Marlin" to listOf("5EXi","Sportster"),
        "Marussia" to listOf("B1","B2"),
        "Maruti" to listOf("1000","800","Alto","Baleno","Esteem","Gypsy","Omni","Versa","Wagon R","Zen"),
        "Maserati" to listOf("228","3200 GT","420","4200 GT","Barchetta Stradale","Biturbo","Bora","Chubasco","Ghibli","GranTurismo","Indy","Karif","Khamsin","Kyalami","Levante","MC12","MC20","Merak","Mexico","Quattroporte","Royale","Shamal"),
        "Matra" to listOf("Murena"),
        "Maybach" to listOf("57","62","Exelero"),
        "Mazda" to listOf("1000","121","1300","2","3","3 MPS","323","5","6","6 MPS","616","626","818","929","Atenza","Autozam AZ-3","Axela","AZ-1","AZ-Offroad","AZ-Wagon","B-series","Biante","Bongo","Bongo Friendee","BT-50","Capella","Carol","Chantez","Cosmo","Cronos","CX-3","CX-30","CX-5","CX-7","CX-8","CX-9","Demio","Efini MS-6","Efini MS-8","Efini MS-9","Etude","Eunos 100","Eunos 300","Eunos 500","Eunos 800","Eunos Cosmo","Familia","Flair","Flair Crossover","Flair Wagon","Lantis","Laputa","Luce","Millenia","MPV","MX-3","MX-30","MX-5","MX-6","Navajo","Persona","Premacy","Proceed","Proceed Levante","Proceed Marvie","Protege","R360","Revue","Roadster","RX-7","RX-8","Scrum","Sentia","Spiano","Tribute","Verisa","Xedos 6","Xedos 9"),
        "McLaren" to listOf("540C","570GT","570S","600LT","650S","675LT","720S","Artura","F1","GT","MP4-12C","P1","Senna"),
        "Mega" to listOf("Club","Monte Carlo","Track"),
        "MercedesBenz" to listOf("190 (W201)","190 SL","220 (W187)","A-klasse","A-klasse AMG","AMG GLC","AMG GLC Coupe","AMG GLE","AMG GLE Coupe","AMG GT","B-klasse","C-klasse","C-klasse AMG","Citan","CL-klasse","CL-klasse AMG","CLA-klasse","CLA-klasse AMG","CLC-klasse","CLK-klasse","CLK-klasse AMG","CLS-klasse","CLS-klasse AMG","E-klasse","E-klasse AMG","EQA","EQB","EQC","EQE","EQS","EQS AMG","EQV","G-klasse","G-klasse AMG","G-klasse AMG 6x6","GL-klasse","GL-klasse AMG","GLA-klasse","GLA-klasse AMG","GLB","GLB AMG","GLC","GLC Coupe","GLE","GLE Coupe","GLK-klasse","GLS-klasse","GLS-klasse AMG","M-klasse","M-klasse AMG","Maybach G 650 Landaulet","Maybach GLS","Maybach S-klasse","Metris","R-klasse","R-klasse AMG","S-klasse","S-klasse AMG","Simplex","SL-klasse","SL-klasse AMG","SLC-klasse","SLC-klasse AMG","SLK-klasse","SLK-klasse AMG","SLR McLaren","SLS AMG","V-klasse","Vaneo","Viano","Vito","W100","W105","W108","W110","W111","W114","W115","W120","W121","W123","W124","W128","W136","W138","W142","W186","W188","W189","W191","W29","X-klasse Concept"),
        "Mercury" to listOf("Capri","Colony Park","Cougar","Eight","Grand Marquis","Marauder","Mariner","Marquis","Milan","Montego","Monterey","Mountaineer","Mystique","Sable","Topaz","Tracer","Villager"),
        "Messerschmitt" to listOf("KR200"),
        "Metrocab" to listOf("Metrocab I","Metrocab II (TTT)"),
        "MG" to listOf("3","350","5","550","6","750","F","GS","Maestro","Metro","MGA","MGB","Midget","Montego","RV8","TD Midget","TF","Xpower SV","ZR","ZS","ZT"),
        "Microcar" to listOf("F8C","M.Go","M8","MC","Virgo"),
        "Minelli" to listOf("TF 1800"),
        "MINI" to listOf("Cabrio","Clubman","Countryman","Coupe","Hatch","Paceman","Roadster"),
        "Mitsubishi" to listOf("3000 GT","500","Airtrek","Aspire","ASX","Attrage","Bravo","Carisma","Celeste","Challenger","Chariot","Colt","Cordia","Debonair","Delica","Delica D:2","Delica D:3","Delica D:5","Diamante","Dignity","Dingo","Dion","Eclipse","Eclipse Cross","eK","eK Active","eK Classic","eK Custom","eK Space","eK Sport","Emeraude","Endeavor","Eterna","Freeca","FTO","Galant","Galant Fortis","Grandis","GTO","i","i-MiEV","Jeep J","L200","L300","L400","Lancer","Lancer Cargo","Lancer Evolution","Lancer Ralliart","Legnum","Libero","Minica","Minicab","Mirage","Montero","Montero Sport","Outlander","Pajero","Pajero iO","Pajero Junior","Pajero Mini","Pajero Pinin","Pajero Sport","Pistachio","Proudia","Raider","RVR","Sapporo","Savrin","Sigma","Space Gear","Space Runner","Space Star","Space Wagon","Starion","Strada","Toppo","Town Box","Tredia","Triton","Xpander"),
        "Mitsuoka" to listOf("Galue","Galue 204","Himiko","Le-Seyde","Like","MC-1","Nouera","Orochi","Ray","Rock Star","Ryoga","Ryugi","Viewt","Yuga","Zero 1"),
        "Morgan" to listOf("3 Wheeler","4 Seater","4/4","Aero 8","Aero Coupe","Aero SuperSports","AeroMax","Plus 4","Plus 8","Roadster"),
        "Morris" to listOf("Eight","Marina"),
        "Moskvich" to listOf("2136","2137","2138","2140","2141","2142","400","401","402","403","407","408","410","411","412","423","424","426","427","430","434P","Duet","Ivan Kalita","Knyazy Vladimir","Svyatogor","Yuriy Dolgorukiy"),
        "Nash" to listOf("Ambassador"),
        "Nio" to listOf("ES8"),
        "Nissan" to listOf("100NX","180SX","200SX","240SX","280ZX","300ZX","350Z","370Z","AD","Almera","Almera Classic","Almera Tino","Altima","Ariya","Armada","Auster","Avenir","Bassara","BE-1","Bluebird","Bluebird Maxima","Bluebird Sylphy","Caravan","Cedric","Cefiro","Cherry","Cima","Clipper","Clipper Rio","Crew","Cube","Datsun","Dayz","Dayz Roox","Dualis","Elgrand","Exa","Expert","Fairlady Z","Figaro","Fuga","Gloria","GT-R","Homy","Hypermini","Juke","Juke Nismo","Kicks","Kix","Lafesta","Langley","Largo","Latio","Laurel","Leaf","Leopard","Liberta Villa","Liberty","Livina","Lucino","March","Maxima","Micra","Mistral","Moco","Murano","Navara (Frontier)","Note","NP 300","NV200","NV300","NV350 Caravan","NX Coupe","Otti (Dayz)","Pao","Pathfinder","Patrol","Pino","Pixo","Prairie","Presage","Presea","President","Primastar","Primera","Pulsar","Qashqai","Qashqai+2","Quest","R'nessa","Rasheen","Rogue","Roox","Safari","Sentra","Serena","Silvia","Skyline","Skyline Crossover","Stagea","Stanza","Sunny","Sylphy Zero Emission","Teana","Terra","Terrano","Terrano Regulus","Tiida","Tino","Titan","Urvan","Vanette","Versa","Wingroad","X-Terra","X-Trail"),
        "Noble" to listOf("M12 GTO","M15","M600"),
        "Oldsmobile" to listOf("Achieva","Alero","Aurora","Bravada","Cutlass","Cutlass Calais","Cutlass Ciera","Cutlass Supreme","Eighty-Eight","Firenza","Intrigue","Ninety-Eight","Omega","Series 60","Series 70","Silhouette","Starfire","Toronado","Vista Cruiser"),
        "Opel" to listOf("Adam","Admiral","Agila","Ampera","Antara","Ascona","Astra","Astra OPC","Calibra","Campo","Cascada","Combo","Commodore","Corsa","Corsa OPC","Crossland X","Diplomat","Frontera","Grandland X","GT","Insignia","Insignia OPC","Kadett","Kapitan","Karl","Manta","Meriva","Meriva OPC","Mokka","Monterey","Monza","Olympia","Omega","P4","Rekord","Senator","Signum","Sintra","Speedster","Super Six","Tigra","Vectra","Vectra OPC","Vita","Vivaro","Zafira","Zafira Life","Zafira OPC"),
        "Osca" to listOf("2500 GT"),
        "Packard" to listOf("200/250","Clipper","Custom Eight","One-Twenty","Six","Twelve"),
        "Pagani" to listOf("Huayra","Zonda"),
        "Panoz" to listOf("Esperante","Roadster"),
        "Perodua" to listOf("Alza","Kancil","Kelisa","Kembara","Kenari","MyVi","Nautica","Viva"),
        "Peugeot" to listOf("1007","104","106","107","108","2008","201","202","203","204","205","205 GTi","206","207","208","208 GTi","3008","301","304","305","306","307","308","308 GTi","309","4007","4008","402","403","404","405","406","407","408","5008","504","505","508","604","605","607","806","807","Bipper","Expert","iOn","Partner","RCZ","Rifter","Traveller"),
        "PGO" to listOf("Cevennes","Hemera","Speedster II"),
        "Piaggio" to listOf("Porter"),
        "Plymouth" to listOf("Acclaim","Barracuda","Breeze","Caravelle","Fury","Horizon","Laser","Neon","Prowler","Reliant","Road Runner","Satellite","Sundance","Turismo","Valiant","Volare","Voyager"),
        "Polestar" to listOf("1","2"),
        "Pontiac" to listOf("6000","Aztek","Bonneville","Catalina","Fiero","Firebird","G4","G5","G6","G8","Grand AM","Grand Prix","GTO","Laurentian","LeMans","Montana","Parisienne","Phoenix","Solstice","Sunbird","Sunfire","Tempest","Torpedo","Torrent","Trans Sport","Vibe","Wave"),
        "Porsche" to listOf("356","718 Boxster","718 Cayman","718 Spyder","911","911 GT2","911 GT3","911 R","912","914","918 Spyder","924","928","944","959","968","Boxster","Carrera GT","Cayenne","Cayenne Coupe","Cayman","Cayman GT4","Macan","Panamera","Taycan"),
        "Premier" to listOf("118NE","Padmini"),
        "Proton" to listOf("Arena","Exora","Gen-2","Inspira","Juara","Perdana","Persona","Preve","Putra","Saga","Satria","Savvy","Tiara","Waja","Wira (400 Series)"),
        "PUCH" to listOf("G-modell","Pinzgauer"),
        "Puma" to listOf("GTB","GTE"),
        "Qoros" to listOf("3","5"),
        "Qvale" to listOf("Mangusta"),
        "RAM" to listOf("1500","ProMaster City"),
        "Rambler" to listOf("Ambassador"),
        "Ravon" to listOf("Gentra","Matiz","Nexia R3","R2","R4"),
        "Reliant" to listOf("Scimitar Sabre"),
        "Renaissance" to listOf("Tropica Roadster"),
        "RenaultSamsung" to listOf("QM5","QM6","SM3","SM5","SM7"),
        "Renault" to listOf("10","11","12","14","15","16","17","18","19","20","21","25","30","4","4CV","5","6","8","9","Alaskan","Arkana","Avantime","Captur","Caravelle","Clio","Clio RS","Clio V6","Dauphine","Dokker","Duster","Espace","Floride","Fluence","Fregate","Fuego","Kadjar","Kangoo","Kaptur","Koleos","KWID","Laguna","Latitude","Lodgy","Logan","Megane","Megane RS","Modus","Rodeo","Safrane","Sandero","Sandero RS","Scenic","Sport Spider","Symbol","Talisman","Trafic","Twingo","Twizy","Vel Satis","Vivastella","Wind","ZOE"),
        "Rezvani" to listOf("Beast","Tank"),
        "Rimac" to listOf("C Two","Nevera"),
        "Rinspeed" to listOf("Chopster"),
        "Roewe" to listOf("E50","Ei5"),
        "RollsRoyce" to listOf("20/25","Camargue","Corniche","Cullinan","Dawn","Ghost","Park Ward","Phantom","Silver Cloud","Silver Ghost","Silver Seraph","Silver Shadow","Silver Spirit","Silver Spur","Silver Wraith","Wraith"),
        "Ronart" to listOf("Lightning"),
        "Rover" to listOf("100","14","200","25","400","45","600","75","800","Maestro","Metro","Mini","Montego","P3","P4","P6","SD1","Streetwise"),
        "Saab" to listOf("600","9-2X","9-3","9-4X","9-5","9-7X","90","900","9000","93","95","96","99","Sonett"),
        "Saipa" to listOf("Saina","Tiba"),
        "Saleen" to listOf("S7"),
        "Santana" to listOf("PS-10"),
        "Saturn" to listOf("Astra","Aura","ION","LS","LW","Outlook","Relay","SC","Sky","SL","SW","VUE"),
        "Scion" to listOf("FR-S","iA","iM","iQ","tC","xA","xB","xD"),
        "Sears" to listOf("Model J"),
        "SEAT" to listOf("133","Alhambra","Altea","Arona","Arosa","Ateca","Cordoba","Exeo","Fura","Ibiza","Ibiza Cupra","Inca","Leon","Leon Cupra","Malaga","Marbella","Mii","Ronda","Tarraco","Toledo"),
        "ShanghaiMaple" to listOf("C31","C32","C61","S51","S52","S81"),
        "ShuangHuan" to listOf("Noble","Sceo"),
        "Simca" to listOf("1300/1500","1307"),
        "Skoda" to listOf("100 Series","105, 120","1200","Citigo","Enyaq","Fabia","Fabia RS","Favorit","Felicia","Forman","Kamiq","Karoq","Kodiaq","Kodiaq RS","Octavia","Octavia RS","Popular","Rapid","Roomster","Scala","Superb","Yeti"),
        "Smart" to listOf("Forfour","Fortwo","Roadster"),
        "SMZ" to listOf("S-1L","S-3A","S-3D","S-3L"),
        "Soueast" to listOf("Lioncel"),
        "Spectre" to listOf("R42"),
        "Spyker" to listOf("C12","C8"),
        "SsangYong" to listOf("Actyon","Actyon Sports","Chairman","Istana","Kallista","Korando","Korando Family","Korando Sports","Korando Turismo","Kyron","Musso","Nomad","Rexton","Rodius","Stavic","Tivoli","XLV"),
        "Steyr" to listOf("1500"),
        "Studebaker" to listOf("Golden Hawk"),
        "Subaru" to listOf("1000","360","Alcyone","Ascent","Baja","Bighorn","Bistro","Brat","BRZ","Dex","Dias Wagon","Domingo","Exiga","Forester","Impreza","Impreza WRX","Impreza WRX STi","Justy","Legacy","Legacy Lancaster","Leone","Levorg","Libero","Lucra","Outback","Pleo","Pleo Plus","R1","R2","Rex","Sambar","Stella","SVX","Traviq","Trezia","Tribeca","Vivio","WRX","WRX STi","XT","XV"),
        "Suzuki" to listOf("Across","Aerio","Alto","Alto Lapin","APV","Baleno","Cappuccino","Cara","Carry","Celerio","Cervo","Cultus","Equator","Ertiga","Escudo","Esteem","Every","Forenza","Fronte","Grand Vitara","Hustler","Ignis","Jimny","Kei","Kizashi","Landy","Liana","MR Wagon","Palette","Reno","Samurai","Sidekick","Solio","Spacia","Splash","Swace","Swift","SX4","Twin","Verona","Vitara","Wagon R","Wagon R+","X-90","Xbee","XL7"),
        "TagAZ" to listOf("Aquila","C-30","C10","C190","Road Partner","Tager","Vega"),
        "Talbot" to listOf("1510","Avenger","Horizon","Rancho","Samba","Solara","Tagora"),
        "TATA" to listOf("Aria","Estate","Indica","Indigo","Nano","Safari","Sierra","Sumo","Sumo Grande","Telcoline","Xenon"),
        "Tatra" to listOf("57","77","80","87","T600","T603","T613","T700"),
        "Tazzari" to listOf("Zero"),
        "Tesla" to listOf("Cybertruck","Model 3","Model S","Model X","Model Y","Roadster"),
        "Think" to listOf("City"),
        "Tianma" to listOf("Century"),
        "Tianye" to listOf("Admiral"),
        "Tofas" to listOf("Kartal","Murat 124","Murat 131","Sahin","Serce"),
        "Toyota" to listOf("2000GT","4Runner","Allex","Allion","Alphard","Altezza","Aqua","Aristo","Aurion","Auris","Avalon","Avanza","Avensis","Avensis Verso","Aygo","bB","Belta","Blade","Blizzard","Brevis","C-HR","Caldina","Cami","Camry","Camry (Japan)","Camry Solara","Carina","Carina E","Carina ED","Cavalier","Celica","Celsior","Century","Chaser","Classic","Comfort","COMS","Corolla","Corolla Cross","Corolla II","Corolla Levin","Corolla Rumion","Corolla Spacio","Corolla Verso","Corona","Corona EXiV","Corsa","Cressida","Cresta","Crown","Crown Majesta","Curren","Cynos","Duet","Echo","Esquire","Estima","Etios","FJ Cruiser","Fortuner","FunCargo","Gaia","Grand HiAce","Granvia","GT86","Harrier","HiAce","Highlander","Hilux","Hilux Surf","Innova","Ipsum","iQ","ISis","Ist","Kluger","Land Cruiser","Land Cruiser Prado","LiteAce","Mark II","Mark X","Mark X ZiO","MasterAce Surf","Matrix","Mega Cruiser","Mirai","Model F","MR-S","MR2","Nadia","Noah","Opa","Origin","Paseo","Passo","Passo Sette","Picnic","Pixis Epoch","Pixis Joy","Pixis Mega","Pixis Space","Pixis Van","Platz","Porte","Premio","Previa","Prius","Prius Alpha","Prius c","Prius v (+)","ProAce","ProAce City","Probox","Progres","Pronard","Publica","Ractis","Raize","Raum","RAV 4","Regius","RegiusAce","Roomy","Rush","Sai","Scepter","Sequoia","Sera","Sienna","Sienta","Soarer","Soluna","Spade","Sparky","Sports 800","Sprinter","Sprinter Carib","Sprinter Marino","Sprinter Trueno","Starlet","Succeed","Supra","Tacoma","Tank","Tercel","Touring HiAce","TownAce","Tundra","Urban Cruiser","Vanguard","Vellfire","Venza","Verossa","Verso","Verso-S","Vios","Vista","Vitz","Voltz","Voxy","V8","WiLL","WiLL Cypha","Windom","Wish","Yaris","Yaris Verso"),
        "Trabant" to listOf("1.1","600","P 601","P50"),
        "Tramontana" to listOf("Tramontana"),
        "Triumph" to listOf("Acclaim","Spitfire","Stag","TR3","TR4","TR6","TR7","TR8"),
        "TVR" to listOf("280","350","390","400","420","450","Cerbera","Chimaera","Griffith","S-Series","Sagaris","Taimar","Tamora","Tasmin","Tuscan"),
        "UAZ" to listOf("3151","3153","3159","3160","3162 Simbir","469","Astero","Hunter","Patriot","Pickup"),
        "Ultima" to listOf("GTR"),
        "Vauxhall" to listOf("Adam","Ampera","Astra","Carlton","Cavalier","Chevette","Corsa","Firenza","Frontera","Insignia","Meriva","Mokka","Monaro","Omega","Royale","Tigra","Vectra","Velox","Ventora","Viceroy","Victor","Viva","VXR8","Zafira"),
        "Vector" to listOf("M12","W8 Twin Turbo"),
        "Venturi" to listOf("210","260 LM","300 Atlantique","400 GT"),
        "Volkswagen" to listOf("181","Amarok","Arteon","Arteon R","Atlas","Atlas Cross Sport","Beetle","Bora","Caddy","California","Caravelle","Corrado","Derby","Eos","EuroVan","Fox","Gol","Golf","Golf Country","Golf GTI","Golf Plus","Golf R","Golf R32","Golf Sportsvan","ID.3","ID.4","Iltis","Jetta","K70","Karmann-Ghia","Lavida","Lupo","Lupo GTI","Multivan","Parati","Passat","Passat (North America)","Passat CC","Phaeton","Pointer","Polo","Polo GTI","Polo R WRC","Quantum","Routan","Santana","Scirocco","Scirocco R","Sharan","SpaceFox","T-Cross","T-Roc","T-Roc R","Taos","Taro","Teramont","Tiguan","Tiguan R","Touareg","Touran","Transporter","Type 166","Type 2","Type 3","Type 4","Type 82","up!","Vento","XL1"),
        "Volvo" to listOf("120 Series","140 Series","164","240 Series","260 Series","300 Series","440","460","480","66","740","760","780","850","940","960","C30","C40","C70","Laplander","P1800","P1900","PV444","PV544","S40","S60","S60 Cross Country","S70","S80","S90","V40","V40 Cross Country","V50","V60","V60 Cross Country","V70","V90","V90 Cross Country","XC40","XC60","XC70","XC90"),
        "Vortex" to listOf("Corda","Estina","Tingo"),
        "WMotors" to listOf("Fenyr Supersport","Lykan Hypersport"),
        "Wanderer" to listOf("W23","W50"),
        "Wartburg" to listOf("1.3","353"),
        "Weltmeister" to listOf("EX5"),
        "Westfield" to listOf("SEi & Sport","SEiGHT"),
        "Wiesmann" to listOf("GT","Roadster"),
        "Willys" to listOf("CJ","Jeepster","Knight Model 20","MB"),
        "XinKai" to listOf("Pickup X3","SR-V X3","SUV X3"),
        "Xpeng" to listOf("G3","P7"),
        "Yomobile" to listOf("E-Krossover"),
        "Yulon" to listOf("Feeling"),
        "Zastava" to listOf("10","Florida","Skala","Yugo"),
        "ZAZ" to listOf("1102 Tavriya","1103 Slavuta","1105 Dana","965","966","968","Chance","Forza","Lanos","Sens","Vida"),
        "Zenos" to listOf("E10"),
        "Zenvo" to listOf("ST1"),
        "Zibar" to listOf("MK2"),
        "ZIL" to listOf("111","114","117","4104"),
        "ZiS" to listOf("101","110"),
        "Zotye" to listOf("Coupa","E200","Nomad (RX6400)","SR9","T600","Z100","Z300"),
        "ZX" to listOf("Admiral","Grand Tiger","Landmark")
    )

}