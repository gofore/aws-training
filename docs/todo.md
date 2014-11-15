
PART 01: Intro

- intro EC2 & IAM + mainitaan kiinnostavimmat palvelut
- luodaan oma tyhjä kone ja avain, ssh:tetaan sisään, curlataan meta dataa

- esitellään tehtävän sovelluksen arkkitehtuuri ja suunnitteluperiaate
- esitellään cloudformation

PART 02: Loose coupling with Queues

- luodaan cloudformationilla simpledb-domain ja sqs-jonot (ilman että näitä on vielä opetettu)
- esitellään sqs ja tutkitaan sitä webbikälistä

- kokeillaan ajaa pelkkää ui:ta omalta koneelta (käyttäen luotuja sdb ja sqs)
- koodataan vähän jotain, esim. toteutus sqs-pollereille
- koodataan viestin lähetys ui:hin

- tutkitaan miten viestinvälitys rullaa --> käynnistetään omalla koneella loader

PART 03: Persistence

- esitellään S3-palvelu
- tutkitaan mitä s3:sta löytyy
- koodataan s3-tallennus

- esitellään simpledb
- tutkitaan mitä simpledb:ssä on
- koodataan simpledb-tallennus

PART 04: Auto Scaling - Lopullisen järjestelmän kokeilu (aloitetaan complete-koodilla)

- esitellään elb ja autoscaling

- (deployataan omat sovellus-jar:it s3:een)

- esitellään lopullinen cloudformation, userdata ja deployment

- luodaan cloudformationilla koko stäkki
- ihmetellään toimintaa ja autoscalausta

