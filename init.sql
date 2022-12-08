CREATE TABLE Team (
    Team_Name VARCHAR(30) NOT NULL,
    Win_Rate DECIMAL(4, 1) NOT NULL,
    CONSTRAINT PK_Team Primary Key(Team_Name)
);

CREATE TABLE Player (
    Player_Name VARCHAR(30) NOT NULL,
    KDA_Ratio DECIMAL(4, 1),
    Win_Rate DECIMAL(4, 1),
    Team_Name VARCHAR(30) NOT NULL,
    CONSTRAINT PK_Player Primary Key(Player_Name),
    CONSTRAINT FK_PlayerTeam Foreign Key(Team_Name)
        REFERENCES Team(Team_Name)
);
    
CREATE TABLE Champion (
    Champion_Name VARCHAR(30) NOT NULL,
    KDA_Ratio DECIMAL(4 , 1),
    Win_Rate DECIMAL(4, 1),
    Ban_Rate DECIMAL(4, 1),
    Pick_Rate DECIMAL(4, 1),
    CONSTRAINT PK_Champion Primary Key(Champion_Name)
);
    
CREATE TABLE Tournament (
    Tournament_Name VARCHAR(30) NOT NULL,
    Location VARCHAR(30) NOT NULL,
    PrizePool INTEGER(1) NOT NULL,
    CONSTRAINT PK_Tournament Primary Key(Tournament_Name)
);

CREATE TABLE ParticipatedInTourney (
    Tournament_Name VARCHAR(30) NOT NULL,
    Team_Name VARCHAR(30) NOT NULL,
    CONSTRAINT PK_ParticipatedInTourney Primary Key(Tournament_Name, Team_Name),
    CONSTRAINT FK_Tourney Foreign Key(Tournament_Name)
        REFERENCES Tournament(Tournament_Name),
    CONSTRAINT FK_Team Foreign Key(Team_Name)
        REFERENCES Team(Team_Name)
);
    

CREATE TABLE Series (
    SeriesID INTEGER(1) NOT NULL,
    Occurred DATE NOT NULL,
    Winner VARCHAR(30) NOT NULL,
    Loser VARCHAR(30) NOT NULL,
    Tournament_Name VARCHAR(30) NOT NULL,
    CONSTRAINT PK_Series Primary Key(SeriesID),
    CONSTRAINT Winner_Must_Participate Foreign Key(Winner, Tournament_Name)
        REFERENCES ParticipatedInTourney(Team_Name, Tournament_Name),
    CONSTRAINT Loser_Must_Participate Foreign Key(Loser, Tournament_name)
        REFERENCES ParticipatedInTourney(Team_Name, Tournament_Name),
    CONSTRAINT Two_Different_Teams CHECK (Winner <> Loser)
);

CREATE TABLE Game (
    GameID INTEGER(1) NOT NULL,
    SeriesID INTEGER(1) NOT NULL,
    Length TIME NOT NULL,
    CONSTRAINT PK_Game Primary Key(GameID),
    CONSTRAINT FK_Series Foreign Key(SeriesID)
        REFERENCES Series(SeriesID)
);
    
CREATE TABLE PlayedIn (
    GameID INTEGER(1) NOT NULL,
    Player_Name VARCHAR(30) NOT NULL,
    Champion_Name VARCHAR(30) NOT NULL,
    KDA_Ratio DECIMAL(4, 1) NOT NULL,
    CONSTRAINT PK_PlayedIn Primary Key(GameID, Player_Name),
    CONSTRAINT FK_Game Foreign Key(GameID)
        REFERENCES Game(GameID),
    CONSTRAINT FK_Player Foreign Key(Player_Name)
        REFERENCES Player(Player_name)
);

INSERT INTO Team
VALUES('The Jalal Omers', 100.0);

INSERT INTO Player
VALUES('Jalal Omer', 100.0, 100.0, "The Jalal Omers");

INSERT INTO Team
VALUES('The Comets', 77.5);

INSERT INTO Player
VALUES('Temoc', 57.5, 91.0, "The Comets");

INSERT INTO Player
VALUES( "Tobor", 14.3, 24.0, "The Comets");

INSERT INTO Player
VALUES( "Enarc", 1.3, 27.0, "The Comets");

INSERT INTO Player
VALUES( "Student Union", 72.9, 64.3, "The Comets");

INSERT INTO Tournament
VALUES('UTD Rumble', "UT Dallas", 12000);

INSERT INTO ParticipatedInTourney
VALUES("UTD Rumble", "The Jalal Omers");

INSERT INTO ParticipatedInTourney
VALUES("UTD Rumble", "The Comets");

INSERT INTO Series
VALUES('1', '2020-11-12', "The Jalal Omers", "The Comets", "UTD Rumble"); 

INSERT INTO Game
VALUES(1,1,1);

INSERT INTO Game
VALUES(2,1,12.3);

INSERT INTO Champion
VALUES('Aatrox', 46.7, 12.3, 01.0, 12.4);

INSERT INTO Champion
VALUES('Garen', 32.7, 27.5, 11.0, 42.4);

INSERT INTO Champion
VALUES('Malphite', 23.3, 75.2, 10.1, 24.2);

INSERT INTO Champion
VALUES('Warwick', 42.6, 72.0, 14.5, 71.2);

INSERT INTO Champion
VALUES('Master Yi', 73.2, 82.4, 02.4, 82.4);

INSERT INTO Champion
VALUES('Annie', 37.2, 42.8, 42.0, 12.2);

INSERT INTO Champion
VALUES('Talon', 12.3, 10.2, 05.4, 7.9);

INSERT INTO PlayedIn
VALUES(1, "Temoc", "Aatrox", 97.4);