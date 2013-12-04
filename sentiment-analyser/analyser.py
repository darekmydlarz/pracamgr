from textblob import TextBlob

tweets = ["We need to do something about this Arsenal problem tho.",
"@piersmorgan so far this season Arsenal have only played one of the teams to finish in the top four from last season and they've lost that",
"Me Beef Arsenal? LMAO...What's There To Beef? RT \@olise__ogbolu: Lol beef @Igbo_Chuck: LOL..ur First Time In This Situation. @olise__ogbolu",
"@astur7 yea I did for a couple of seasons. Feel like a dick hahaha. Least wenger knew as he always does ;) what a game today COYG",
"@_griff Oh mate, it's *almost* like the last 8 years were fine. I adore watching arsenal at the moment. I don't feel sick or nervous at all!",
"Arsenal's just getting better & better. 🙌",
"@pmezza1970 facts are there..take it or leave it..he has scored 8..giroud on same i believe ..",
"SOLID ROCK! \"@shinagooner: Koscielny + Mertesacker = 0 goal conceded in last 6 games played together. #Respect #afc\"",
"We are the very much on top of the league. What a performance by my Arsenal. I'm loving it.",
"Yes Get In Arsenal! Well done lads #comeonthegunners :)",
"\"@DrChubbyy: Cardiff beat City. You beat Cardiff. We beat you.\" AND WE ARE HOW MANY POINTS ABOVE YOU GUYS AGAIN?? HUH CUNT JOKES ON YOU",
"As of today, Wojciech Szczesny has 51 clean sheets in 126 appearances for #arsenal #ARSCAR",
"@Ifreke best buy from Wenger in a while",
"@Luis_Are5 okay there you arsenal fans go again.. Taking it too far!",
"Arsenal tu win again *sigh*",
"There is no need for snide remarks at other teams every time #Arsenal wins",
"He made it to the international news! RT @AJEnglish Ramsey scores twice in Arsenal victory http://t.co/Ia8IcgHWiL",
"Watching Arsenal whilst not checking twitter is the best! Been doing it most games :) #LevelHeaded",
"@piersmorgan @benwinston Comes to something when even the Arsenal fans think you're a moron Morgan.@0SAIDS @0SAIDS",
"AND, Ramsey's only 22yrs old...Remember?"]

print(len(tweets))
for tweet in tweets:
    print(TextBlob(tweet).polarity, tweet)