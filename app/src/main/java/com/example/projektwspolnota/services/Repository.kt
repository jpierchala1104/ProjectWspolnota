package com.example.projektwspolnota.services

import com.example.projektwspolnota.resolution.mvi.ResolutionItem

object Repository {
    private var resolutions: Collection<ResolutionItem> = listOf(
        ResolutionItem(1, "Uchwała o ścięcie drzewa", "10.06.2019", "17.06.2019"),
        ResolutionItem(2, "Uchwała o śmieciach", "20.06.2019", "27.06.2019"),
        ResolutionItem(3, "Uchwała o remoncie", "01.07.2019", "08.07.2019"),
        ResolutionItem(4, "Uchwała dotycząca wysokości opłat", "02.07.2019", "10.07.2019"),
        ResolutionItem(5, "Uchwała dotycząca finansów", "05.07.2019", "13.07.2019")
    )
    private var points: Collection<Point> = listOf(
        Point(1, 1, "Ścięcie drzewa przy wejsciu", 1),
        Point(2, 2, "Ścięcie drzewa za głównym budynkiem", 1),
        Point(3, 3, "Ścięcie drzewa przy placu zabaw", 1),
        Point(4, 1, "Segregacja śmieci obowiązkowa", 2),
        Point(5, 2, "Zmiana koszy na nowe", 2),
        Point(6, 1, "Remont w 1 miesiąc wakacji", 3),
        Point(7, 2, "Remont w 2 miesiąc wakacji", 3),
        Point(8, 3, "Remont w soboty", 3),
        Point(9, 4, "Dodatkowy remont korytarza", 3),
        Point(10, 1, "Zwiekszenie opłat na czas remontu", 4),
        Point(11, 2, "Składka na remont bez zwiekszania opłat", 4),
        Point(12, 1, "w/s zatwierdzenia rozliczenia rzeczowo-finansowego za 2018r. i " +
                "udzielenia absolutorium Zarządowi za okres sprawozdawczy.",5),
        Point(13, 2, "w/s przyjęcia Planu Finansowo - Gospodarczego na 2019r czyli " +
                "ustala się wysokość opłat w formie zaliczki na pokrycie kosztów zarządu nieruchomością wspólną " +
                "w kwocie miesięcznie 17 260 zł tj.172,20 zł/udział w nieruchomości wspólnej.",5),
        Point(14, 3, "w/s wysokości wypłat na Fundusz Remontowy tj. ustala się wpłaty w " +
                "wysokości 10 000 zł/m-c tj 100,00 zł/udział w nieruchomości wspólnej, z przeznaczeniem na planowane " +
                "remonty oraz zatwierdza się Plan Remontów na 2019r.: \n" +
                "-  kontynuacja prac remontowych zatwierdzonych w roku poprzednim (min. izolacja i osuszanie " +
                "budynków w razie potrzeby, powiększenie wiaty śmietnikowej, zabezpieczenia p/k gołębiom) \n" +
                "-  sukcesywna wymiana/renowacja desek elewacyjnych oraz na murkach przy weksciach do klatek" +
                "schodowych - zadanie podzielone na 3-4 lata \n" +
                "-  sukcesywna wymiana desek na balustradach balkonów wraz z malowaniem balustrad - zadanie" +
                "podzielone na 3-4 lata",5)
    )
    private var userPoints: Collection<UserPoint> = listOf(
        UserPoint(1, 1, true),
        UserPoint(1, 2, false),
        UserPoint(1, 3, false),
        UserPoint(1, 4, true),
        UserPoint(1, 5, true),
        UserPoint(2, 1, false),
        UserPoint(2, 2, false),
        UserPoint(2, 3, false),
        UserPoint(2, 4, null),
        UserPoint(2, 5, true),
        UserPoint(3, 1, true),
        UserPoint(3, 2, false),
        UserPoint(3, 3, true),
        UserPoint(3, 4, true),
        UserPoint(3, 5, true),
        UserPoint(4, 1, null),
        UserPoint(4, 2, false),
        UserPoint(4, 3, true),
        UserPoint(4, 4, false),
        UserPoint(4, 5, false),
        UserPoint(5, 1, true),
        UserPoint(5, 2, false),
        UserPoint(5, 3, true),
        UserPoint(5, 4, null),
        UserPoint(5, 5, null)
    )

    fun getAllResolutions(): Collection<ResolutionItem> {
        return resolutions
    }

    fun getAllPoints(): Collection<Point> {
        return points
    }

    fun getAllUserPoints(): Collection<UserPoint> {
        return userPoints
    }

    fun getResolutionById(id: Int): ResolutionItem {
        return resolutions.first { x ->
            x.resolutionId == id
        }
    }

    fun getPointsInResolution(id: Int): Collection<Point> {
        return points
            .filter { x ->
                x.resolutionId == id
            }
    }

    fun getUserPointsByPointId(id: Int): Collection<UserPoint> {
        return userPoints.filter {
            x -> x.pointId == id
        }
    }

    fun saveVote(userId: Int, pointId: Int, vote: Boolean?){
        userPoints = userPoints.plus(
            UserPoint(userId, pointId, vote)
        )
    }
}