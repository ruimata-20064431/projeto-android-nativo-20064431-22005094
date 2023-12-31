package pt.ulusofona.deisi.cm2223.g20064431_22005094.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// - A gestão de classes na API 23 é... limitada.
// - Não existe acesso ao tipo LocalDate, e ao dia de hoje já existem algumas recomendações sobre a não-utilização
//    de determinadas classes que neste ponto vigoravam.
// - O que se pretende nesta classe é, simplesmente, procurar reduzir o boilerplate que envolve a gestão
//    de Datas, utilizando um tipo "customizado" de dados.
// - Feito com ligeiro recurso ao ChatGPT, usando queries semelhantes a:
//    "How to manage Android dates on API level 23?"; "How to get the device's current date in Android API 23?", etc.

class CustomDate(dateInMillis: Long = System.currentTimeMillis()) {
    // Para armazenar a Data
    private val calendar = Calendar.getInstance()
    private val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    // - Nesta classe é possível instanciar um objeto com uma Data predefinida,
    //     preenchendo o parâmetro [dateInMillis] (data pretendida em milissegundos).
    // - Caso não seja fornecido, predefine para a data atual no dispositivo.
    init {
        calendar.timeInMillis = dateInMillis
    }

    fun getYear(): Int = calendar.get(Calendar.YEAR)
    fun getMonth(): Int = calendar.get(Calendar.MONTH)  // NOTA: O mês é Zero-based! (0 = Janeiro)
    fun getDayOfMonth(): Int = calendar.get(Calendar.DAY_OF_MONTH)

    // fun getDateInMillis(): Long = calendar.timeInMillis

    // Adiciona (se positivo) ou retira (se negativo) X dias à instância.
    fun addDays(nrOfDays: Int) = calendar.add(Calendar.DAY_OF_MONTH, nrOfDays)

    fun setDateTo(ano: Int, mes: Int, dia: Int) {
        calendar.set(Calendar.YEAR, ano)
        calendar.set(Calendar.MONTH, mes)
        calendar.set(Calendar.DAY_OF_MONTH, dia)
    }

    // Devolve a data em formato Long (millis)
    fun toMillis(): Long = calendar.timeInMillis

    // Escreve no formato dd/MM/yyyy, ex: "31/12/1990"
    override fun toString(): String = sdf.format(calendar.time)

    companion object {
        // Para usar quando se lê a data da API (Release date do filme)
        // Credits ChatGPT @ query que pede como fazer parse deste formato para Calendar
        @JvmStatic
        fun fromHumanReadableDate(dtString: String): CustomDate {
            // O formato aqui esperado é o "dd MMM yyyy" (que vem da API).
            // Por exemplo: "18 Dec 2009"
            try {
                val dtParser = SimpleDateFormat("dd MMM yyyy", Locale.US)
                val dtParsed = dtParser.parse(dtString)
                return CustomDate(dtParsed.time)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Devolve uma data de fallback se o parse falhar
            // (facilmente se verificará que ocorreu um erro neste parâmetro)
            return CustomDate(0L)
        }
    }

}