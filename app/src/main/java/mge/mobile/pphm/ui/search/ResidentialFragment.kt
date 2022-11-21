package mge.mobile.pphm.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SeekBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import mge.mobile.pphm.R
import mge.mobile.pphm.data.model.Search
import mge.mobile.pphm.databinding.FragmentResidentialBinding
import mge.mobile.pphm.util.Validator
import mge.mobile.pphm.util.ViewModelFactory

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class ResidentialFragment(private val viewModel: SearchViewModel) : Fragment() {

    private var answer1 = ""
    private var answer2 = ""
    private var answer3 = ""
    private var answer4 = ""
    private var answer5 = ""
    private var answer6 = ""
    private var answer7 = ""
    private var answer8 = ""
    private var answer9 = ""
    private var answer10 = ""
    private var answer11 = ""

    private var telefone = ""
    private var email = ""

    lateinit var cbManha: CheckBox
    lateinit var cbTarde: CheckBox
    lateinit var cbNoite: CheckBox
    lateinit var cbMadrugada: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentResidentialBinding.inflate(inflater, container, false)
        activity?.let {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
            this.lifecycle.addObserver(viewModel)

            // Nome do Entrevistado
            binding.residentialSearchQuestion1Answer.addTextChangedListener {
                answer1 = it.toString()
                viewModel.search1.postValue(viewModel.buildSearch(1, answer1))
            }

            // É o titual da fatura de energia
            binding.residentialSearchQuestion2Answer1.setOnCheckedChangeListener { _, checkedId ->
                answer2 = when (checkedId) {
                    R.id.residential_search_question_2_answer_1_1 -> binding.residentialSearchQuestion2Answer11.text.toString()
                    R.id.residential_search_question_2_answer_1_2 -> binding.residentialSearchQuestion2Answer12.text.toString()
                    else -> ""
                }
                viewModel.search2.postValue(viewModel.buildSearch(2, answer2))
            }

            // Telefone
            binding.residentialSearchQuestion3Answer.addTextChangedListener {
                telefone = it.toString()
                Log.i("test->", it.toString())
                answer3 = telefone
                viewModel.search3.postValue(viewModel.buildSearch(3, answer3))
            }

            // 1. O que caracteriza o consumo de energia do seu domicilio ?
            binding.residentialSearchQuestion4Answer1.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.residential_search_question_4_answer_1_1 -> {
                        binding.residentialSearchQuestion4Answer2.visibility = View.VISIBLE
                        answer4 = ""
                    }
                    R.id.residential_search_question_4_answer_1_2 -> {
                        binding.residentialSearchQuestion4Answer2.visibility = View.GONE
                        answer4 = binding.residentialSearchQuestion5Answer12.text.toString()
                    }
                    R.id.residential_search_question_4_answer_1_3 -> {
                        binding.residentialSearchQuestion4Answer2.visibility = View.GONE
                        answer4 = binding.residentialSearchQuestion4Answer13.text.toString()
                    }
                }
                Log.i("test->", answer4)
                viewModel.search4.postValue(viewModel.buildSearch(4, answer4))
            }

            binding.residentialSearchQuestion4Answer2.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.residential_search_question_4_answer_2_1 -> {
                        answer4="Residencial;" + binding.residentialSearchQuestion4Answer21.text.toString()
                    }

                    R.id.residential_search_question_4_answer_2_2 -> {
                        answer4="Residencial;" + binding.residentialSearchQuestion4Answer22.text.toString()
                    }
                    R.id.residential_search_question_4_answer_2_3 -> {
                        answer4="Residencial;" + binding.residentialSearchQuestion4Answer23.text.toString()
                    }
                }
                Log.i("test->", answer4)
                viewModel.search4.postValue(viewModel.buildSearch(4, answer4))
            }

            // 2. Geralmente, quais os períodos do dia o consumo é maior ?
            binding.residentialSearchQuestion5Answer11.setOnClickListener(View.OnClickListener {
                if(binding.residentialSearchQuestion5Answer11.isChecked){
                    if(!answer5.contains("Manhã;")){
                        answer5 = "Manhã;" + answer5
                    }
                }
                else{
                    if(answer5.contains("Manhã;")){
                        answer5 = answer5.replace("Manhã;", "");
                    }
                }

                viewModel.search5.postValue(viewModel.buildSearch(5, answer5))
            })

            binding.residentialSearchQuestion5Answer12.setOnClickListener(View.OnClickListener {
                if(binding.residentialSearchQuestion5Answer12.isChecked){
                    if(!answer5.contains("Tarde;")){
                        answer5 = answer5 + "Tarde;"
                    }
                }
                else{
                    if(answer5.contains("Tarde;")){
                        answer5 = answer5.replace("Tarde;", "");
                    }
                }

                viewModel.search5.postValue(viewModel.buildSearch(5, answer5))
            })

            binding.residentialSearchQuestion5Answer13.setOnClickListener(View.OnClickListener {
                if(binding.residentialSearchQuestion5Answer13.isChecked){
                    if(!answer5.contains("Noite;")){
                        answer5 = answer5 + "Noite;"
                    }
                }
                else{
                    if(answer5.contains("Noite;")){
                        answer5 = answer5.replace("Noite;", "");
                    }
                }

                viewModel.search5.postValue(viewModel.buildSearch(5, answer5))
            })

            binding.residentialSearchQuestion5Answer14.setOnClickListener(View.OnClickListener {
                if(binding.residentialSearchQuestion5Answer14.isChecked){
                    if(!answer5.contains("Madrugada;")){
                        answer5 = answer5 + "Madrugada"
                    }
                }
                else{
                    if(answer5.contains("Madrugada;")){
                        answer5 = answer5.replace("Madrugada;", "");
                    }
                }

                viewModel.search5.postValue(viewModel.buildSearch(5, answer5))
            })

            // 3.Quantas pessoas residem no local ?
            binding.residentialSearchQuestion6Answer.addTextChangedListener {
                answer6 = it.toString()
                viewModel.search6.postValue(viewModel.buildSearch(6, answer6))
            }

            // 4. Sabe qual é o valor da sua tarifa de energia elétrica atual?
            binding.residentialSearchQuestion7Answer1.setOnCheckedChangeListener { _, checkedId ->
                answer7 = when (checkedId) {
                    R.id.residential_search_question_7_answer_1_1 -> binding.residentialSearchQuestion7Answer11.text.toString()
                    R.id.residential_search_question_7_answer_1_2 -> binding.residentialSearchQuestion7Answer12.text.toString()
                    else -> ""
                }
                viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
            }

            // 5. Caso ocorra uma mudança nas tarifas de energia de tal modo que a tarifa de energia para uso das 20:00 às 23:00 horas fosse 5 vezes mais cara, você:
            binding.residentialSearchQuestion8Answer1.setOnCheckedChangeListener { _, checkedId ->
                answer8 = when (checkedId) {
                    R.id.residential_search_question_8_answer_1_1 -> binding.residentialSearchQuestion8Answer11.text.toString()
                    R.id.residential_search_question_8_answer_1_2 -> binding.residentialSearchQuestion8Answer12.text.toString()
                    R.id.residential_search_question_8_answer_1_3 -> binding.residentialSearchQuestion8Answer13.text.toString()
                    else -> ""
                }
                viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
            }

            // 6. Se houvesse um desconto de 10% na tarifa de energia para uso fora do horário das 20:00 às 23:00 horas você:
            binding.residentialSearchQuestion9Answer1.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.residential_search_question_9_answer_1_1 -> {
                        answer9 = binding.residentialSearchQuestion9Answer11.text.toString()
                    }
                    R.id.residential_search_question_9_answer_1_2 -> {
                        answer9 = binding.residentialSearchQuestion9Answer12.text.toString()
                    }
                    R.id.residential_search_question_9_answer_1_3 -> {
                        answer9 = binding.residentialSearchQuestion9Answer12.text.toString()
                    }
                }
                Log.i("test->", answer9)
                viewModel.search9.postValue(viewModel.buildSearch(9, answer9))
            }

            // 7. Se ganhasse um aparelho de ar condicionado portátil, para ser ligado numa tomada, e seu uso resultasse num
            // aumento médio de 80,00 oitenta reais na conta mensal de energia:
            binding.residentialSearchQuestion10Answer1.setOnCheckedChangeListener { _, checkedId ->
                answer10 = when (checkedId) {
                    R.id.residential_search_question_10_answer_1_1 -> binding.residentialSearchQuestion10Answer11.text.toString()
                    R.id.residential_search_question_10_answer_1_2 -> binding.residentialSearchQuestion10Answer12.text.toString()
                    R.id.residential_search_question_10_answer_1_3 -> binding.residentialSearchQuestion10Answer13.text.toString()
                    R.id.residential_search_question_10_answer_1_4 -> binding.residentialSearchQuestion10Answer14.text.toString()
                    else -> ""
                }
                viewModel.search10.postValue(viewModel.buildSearch(10, answer10))
            }

            // 8. Você já recebeu alguma orientação por parte da distribuidora de como economizar energia elétrica?
            // E você pratica a economia de energia elétrica no seu dia a dia?
            binding.residentialSearchQuestion11Answer1.setOnCheckedChangeListener { _, checkedId ->
                answer11 = when (checkedId) {
                    R.id.residential_search_question_11_answer_1_1 -> binding.residentialSearchQuestion11Answer11.text.toString()
                    R.id.residential_search_question_11_answer_1_2 -> binding.residentialSearchQuestion11Answer12.text.toString()
                    R.id.residential_search_question_11_answer_1_3 -> binding.residentialSearchQuestion11Answer13.text.toString()
                    R.id.residential_search_question_11_answer_1_4 -> binding.residentialSearchQuestion11Answer14.text.toString()
                    else -> ""
                }
                viewModel.search11.postValue(viewModel.buildSearch(11, answer11))
            }
        }
        return binding.root
    }
}