
package mge.mobile.pphm.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.widget.addTextChangedListener
import mge.mobile.pphm.R
import mge.mobile.pphm.databinding.FragmentCommercialIndustrialBinding
import mge.mobile.pphm.databinding.FragmentResidentialBinding
import org.jetbrains.anko.internals.AnkoInternals.createAnkoContext

class CommercialIndustrialFragment(private val viewModel: SearchViewModel) : Fragment() {

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


   /* private var answer1 = ""
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
    private var answer12 = ""
    private var answer13 = ""
    private var answer14 = ""
    private var answer15 = ""
    private var answer16 = ""
    private var answer17 = ""

    private var telefone = ""
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCommercialIndustrialBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        this.lifecycle.addObserver(viewModel)

        binding.industrialCommercialSearchQuestion1Answer.addTextChangedListener {
            answer1 = it.toString()
            viewModel.search1.postValue(viewModel.buildSearch(1, answer1))
        }

        binding.industrialCommercialSearchQuestion2Answer1.setOnCheckedChangeListener { _, checkedId ->
            answer2 = when (checkedId) {
                R.id.industrial_commercial_search_question_2_answer_1_1 -> binding.industrialCommercialSearchQuestion2Answer11.text.toString()
                R.id.industrial_commercial_search_question_2_answer_1_2 -> binding.industrialCommercialSearchQuestion2Answer12.text.toString()
                else -> ""
            }
            viewModel.search2.postValue(viewModel.buildSearch(2, answer2))
        }

        binding.industrialCommercialSearchQuestion3Answer.addTextChangedListener {
            telefone = it.toString()
            Log.i("test->", it.toString())
            answer3 = telefone
            viewModel.search3.postValue(viewModel.buildSearch(3, answer3))
        }

        binding.industrialCommercialSearchQuestion4Answer1.setOnCheckedChangeListener {  _, checkedId ->
            answer4 = when(checkedId) {
                R.id.industrial_commercial_search_question_4_answer_1_1 -> binding.industrialCommercialSearchQuestion4Answer11.text.toString()
                R.id.industrial_commercial_search_question_4_answer_1_2 -> binding.industrialCommercialSearchQuestion4Answer12.text.toString()
                R.id.industrial_commercial_search_question_4_answer_1_3 -> binding.industrialCommercialSearchQuestion4Answer13.text.toString()
                else -> ""
            }
            viewModel.search4.postValue(viewModel.buildSearch(4, answer4))
        }

        binding.industrialCommercialSearchQuestion5Answer1. { _, checkedId ->
            answer5 = when (checkedId) {
                R.id.industrial_commercial_search_question_5_answer_1_1 -> binding.industrialCommercialSearchQuestion5Answer11.text.toString()
                R.id.industrial_commercial_search_question_5_answer_1_2 -> binding.industrialCommercialSearchQuestion5Answer12.text.toString()
                R.id.industrial_commercial_search_question_5_answer_1_3 -> binding.industrialCommercialSearchQuestion5Answer13.text.toString()
                R.id.industrial_commercial_search_question_5_answer_1_4 -> binding.industrialCommercialSearchQuestion5Answer14.text.toString()
                else -> ""
            }
            viewModel.search5.postValue(viewModel.buildSearch(5, answer5))
        }

        binding.industrialCommercialSearchQuestion6Answer.addTextChangedListener {
            answer6 = it.toString()
            viewModel.search6.postValue(viewModel.buildSearch(6, answer6))
        }

        binding.industrialCommercialSearchQuestion7Answer1.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion7Answer1.isChecked){
                if(!answer7.contains("Segunda-feira;")){
                    answer7 = "Segunda-feira;" + answer7
                }
            }
            else{
                if(answer7.contains("Segunda-feira;")){
                    answer7 = answer7.replace("Segunda-feira;", "");
                }
            }

            viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
        })

        binding.industrialCommercialSearchQuestion7Answer2.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion7Answer2.isChecked){
                if(!answer7.contains("Terça-feira;")){
                    answer7 = answer7 + "Terça-feira;"
                }
            }
            else{
                if(answer7.contains("Terça-feira;")){
                    answer7 = answer7.replace("Terça-feira;", "");
                }
            }

            viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
        })

        binding.industrialCommercialSearchQuestion7Answer3.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion7Answer3.isChecked){
                if(!answer7.contains("Quarta-feira;")){
                    answer7 = answer7 + "Quarta-feira;"
                }
            }
            else{
                if(answer7.contains("Quarta-feira;")){
                    answer7 = answer7.replace("Quarta-feira;", "");
                }
            }

            viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
        })

        binding.industrialCommercialSearchQuestion7Answer4.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion7Answer4.isChecked){
                if(!answer7.contains("Quinta-feira;")){
                    answer7 = answer7 + "Quinta-feira;"
                }
            }
            else{
                if(answer7.contains("Quinta-feira;")){
                    answer7 = answer7.replace("Quinta-feira;", "");
                }
            }

            viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
        })

        binding.industrialCommercialSearchQuestion7Answer5.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion7Answer5.isChecked){
                if(!answer7.contains("Sexta-feira;")){
                    answer7 = answer7 + "Sexta-feira;"
                }
            }
            else{
                if(answer7.contains("Sexta-feira;")){
                    answer7 = answer7.replace("Sexta-feira;", "");
                }
            }

            viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
        })

        binding.industrialCommercialSearchQuestion7Answer6.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion7Answer6.isChecked){
                if(!answer7.contains("Sabado;")){
                    answer7 = answer7 + "Sabado;"
                }
            }
            else{
                if(answer7.contains("Sabado;")){
                    answer7 = answer7.replace("Sabado;", "");
                }
            }

            viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
        })

        binding.industrialCommercialSearchQuestion7Answer7.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion7Answer7.isChecked){
                if(!answer7.contains("Domingo;")){
                    answer7 = answer7 + "Domingo;"
                }
            }
            else{
                if(answer7.contains("Domingo;")){
                    answer7 = answer7.replace("Domingo;", "");
                }
            }

            viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
        })

        binding.industrialCommercialSearchQuestion7Answer8.addTextChangedListener {
            if(it.toString().contains("h")){
                answer7 = answer7 + it.toString()
                viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
            }
        }

        binding.industrialCommercialSearchQuestion7Answer9.addTextChangedListener {
            if(it.toString().contains("h")){
                answer7 = answer7 + it.toString()
                viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
            }
        }

        binding.industrialCommercialSearchQuestion8Answer1.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion8Answer1.isChecked){
                if(!answer8.contains("Manhã;")){
                    answer8 = "Manhã;" + answer8
                }
            }
            else{
                if(answer8.contains("Manhã;")){
                    answer8 = answer8.replace("Manhã;", "");
                }
            }

            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.industrialCommercialSearchQuestion8Answer2.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion8Answer2.isChecked){
                if(!answer8.contains("Tarde;")){
                    answer8 = answer8 + "Tarde;"
                }
            }
            else{
                if(answer8.contains("Tarde;")){
                    answer8 = answer8.replace("Tarde;", "");
                }
            }

            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.industrialCommercialSearchQuestion8Answer3.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion8Answer3.isChecked){
                if(!answer8.contains("Noite;")){
                    answer8 = answer8 + "Noite;"
                }
            }
            else{
                if(answer8.contains("Noite;")){
                    answer8 = answer8.replace("Noite;", "");
                }
            }

            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.industrialCommercialSearchQuestion8Answer4.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion8Answer4.isChecked){
                if(!answer8.contains("Madrugada;")){
                    answer8 = answer8 + "Madrugada;"
                }
            }
            else{
                if(answer8.contains("Madrugada;")){
                    answer8 = answer8.replace("Madrugada;", "");
                }
            }

            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.industrialCommercialSearchQuestion9Answer1.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion9Answer1.isChecked){
                answer9 = "Não" + answer9
                binding.industrialCommercialSearchQuestion9Answer2.setEnabled(false)
                binding.industrialCommercialSearchQuestion9Answer2.setChecked(false)
                binding.industrialCommercialSearchQuestion9Answer3.setEnabled(false)
                binding.industrialCommercialSearchQuestion9Answer3.setChecked(false)
                binding.industrialCommercialSearchQuestion9Answer4.setEnabled(false)
                binding.industrialCommercialSearchQuestion9Answer4.setChecked(false)
                binding.industrialCommercialSearchQuestion9Answer5.setEnabled(false)
                binding.industrialCommercialSearchQuestion9Answer5.setChecked(false)
                binding.industrialCommercialSearchQuestion9Answer6.setEnabled(false)
                binding.industrialCommercialSearchQuestion9Answer6.setChecked(false)
                binding.industrialCommercialSearchQuestion9Answer7.setEnabled(false)
                binding.industrialCommercialSearchQuestion9Answer7.setChecked(false)
                binding.industrialCommercialSearchQuestion9Answer8.setEnabled(false)
                binding.industrialCommercialSearchQuestion9Answer8.setChecked(false)
            }
            else{
                if(answer9.contains("Não")){
                    answer9 = answer9.replace("Não;", "");
                    binding.industrialCommercialSearchQuestion9Answer2.setEnabled(true)
                    binding.industrialCommercialSearchQuestion9Answer3.setEnabled(true)
                    binding.industrialCommercialSearchQuestion9Answer4.setEnabled(true)
                    binding.industrialCommercialSearchQuestion9Answer5.setEnabled(true)
                    binding.industrialCommercialSearchQuestion9Answer6.setEnabled(true)
                    binding.industrialCommercialSearchQuestion9Answer7.setEnabled(true)
                    binding.industrialCommercialSearchQuestion9Answer8.setEnabled(true)
                }
            }
            viewModel.search9.postValue(viewModel.buildSearch(9, answer9))
        })

        binding.industrialCommercialSearchQuestion9Answer2.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion9Answer2.isChecked){
                answer9 = answer9 + "Energia Elétrica por placas solares;"
                binding.industrialCommercialSearchQuestion9Answer1.setEnabled(false)
            }
            else{
                if(answer9.contains("Energia Elétrica por placas solares;")){
                    answer9 = answer9.replace("Energia Elétrica por placas solares;", "");
                    if((!binding.industrialCommercialSearchQuestion9Answer3.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer4.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer5.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer6.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer7.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer8.isEnabled)){
                        binding.industrialCommercialSearchQuestion9Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search9.postValue(viewModel.buildSearch(9, answer9))
        })

        binding.industrialCommercialSearchQuestion9Answer3.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion9Answer3.isChecked){
                answer9 = answer9 + "Energia Térmica (aquecimento) por placas solares;"
                binding.industrialCommercialSearchQuestion9Answer1.setEnabled(false)
            }
            else{
                if(answer9.contains("Energia Térmica (aquecimento) por placas solares;")){
                    answer9 = answer9.replace("Energia Térmica (aquecimento) por placas solares;", "");
                    if((!binding.industrialCommercialSearchQuestion9Answer2.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer4.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer5.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer6.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer7.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer8.isEnabled)) {
                        binding.industrialCommercialSearchQuestion9Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search9.postValue(viewModel.buildSearch(9, answer9))
        })

        binding.industrialCommercialSearchQuestion9Answer4.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion9Answer4.isChecked){
                answer9 = answer9 + "Energia Elétrica por queima de combustível (gerador a diesel);"
                binding.industrialCommercialSearchQuestion9Answer1.setEnabled(false)
            }
            else{
                if(answer9.contains("Energia Elétrica por queima de combustível (gerador a diesel);")){
                    answer9 = answer9.replace("Energia Elétrica por queima de combustível (gerador a diesel);", "");
                    if((!binding.industrialCommercialSearchQuestion9Answer2.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer3.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer5.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer6.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer7.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer8.isEnabled)) {
                        binding.industrialCommercialSearchQuestion9Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search9.postValue(viewModel.buildSearch(9, answer9))
        })

        binding.industrialCommercialSearchQuestion9Answer5.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion9Answer5.isChecked){
                answer9 = answer9 + "Gás Encanado (para aquecimento);"
                binding.industrialCommercialSearchQuestion9Answer1.setEnabled(false)
            }
            else{
                if(answer9.contains("Gás Encanado (para aquecimento);")){
                    answer9 = answer9.replace("Gás Encanado (para aquecimento);", "");
                    if((!binding.industrialCommercialSearchQuestion9Answer2.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer3.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer4.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer6.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer7.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer8.isEnabled)) {
                        binding.industrialCommercialSearchQuestion9Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search9.postValue(viewModel.buildSearch(9, answer9))
        })

        binding.industrialCommercialSearchQuestion9Answer6.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion9Answer6.isChecked){
                answer9 = answer9 + "Gás Envasado (botijão) qu enão seja o de cozinha;"
                binding.industrialCommercialSearchQuestion9Answer1.setEnabled(false)
            }
            else{
                if(answer9.contains("Gás Envasado (botijão) qu enão seja o de cozinha;")){
                    answer9 = answer9.replace("Gás Envasado (botijão) qu enão seja o de cozinha;", "");
                    if((!binding.industrialCommercialSearchQuestion9Answer2.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer3.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer4.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer5.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer7.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer8.isEnabled)) {
                        binding.industrialCommercialSearchQuestion9Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search9.postValue(viewModel.buildSearch(9, answer9))
        })

        binding.industrialCommercialSearchQuestion9Answer7.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion9Answer7.isChecked){
                answer9 = answer9 + "Quemia a lenha;"
                binding.industrialCommercialSearchQuestion9Answer1.setEnabled(false)
            }
            else{
                if(answer9.contains("Queima a lenha;")){
                    answer9 = answer9.replace("Queima a lenha;", "");
                    if((!binding.industrialCommercialSearchQuestion9Answer2.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer3.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer4.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer5.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer6.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer8.isEnabled)) {
                        binding.industrialCommercialSearchQuestion9Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search9.postValue(viewModel.buildSearch(9, answer9))
        })

        binding.industrialCommercialSearchQuestion9Answer8.setOnClickListener(View.OnClickListener {
            if(binding.industrialCommercialSearchQuestion9Answer8.isChecked){
                answer9 = answer9 + "Outra;"
                binding.industrialCommercialSearchQuestion9Answer1.setEnabled(false)
                binding.industrialCommercialSearchQuestion9Answer9.visibility = View.VISIBLE
            }
            else{
                var TextoAux : String
                TextoAux = answer9.substring(answer9.indexOf("Outra;"), answer9.length)
                if(answer9.contains("Outra;")){
                    answer9 = answer9.replace(
                        TextoAux, "");
                    binding.industrialCommercialSearchQuestion9Answer9.setText("")
                    binding.industrialCommercialSearchQuestion9Answer9.visibility = View.GONE
                    if((!binding.industrialCommercialSearchQuestion9Answer2.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer3.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer4.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer5.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer6.isEnabled)&&
                        (!binding.industrialCommercialSearchQuestion9Answer7.isEnabled)) {
                        binding.industrialCommercialSearchQuestion9Answer1.setEnabled(true)
                    }
                }
            }
            viewModel.search9.postValue(viewModel.buildSearch(9, answer9))
        })

        binding.industrialCommercialSearchQuestion9Answer9.setOnClickListener(View.OnClickListener {
            answer9 = answer9 + binding.industrialCommercialSearchQuestion9Answer9.text.toString()
        })

        binding.industrialCommercialSearchQuestion10Answer1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.industrial_commercial_search_question_10_answer_1_1 -> {
                    binding.industrialCommercialSearchQuestion10Answer2.visibility = View.GONE
                    answer10 = binding.industrialCommercialSearchQuestion10Answer12.text.toString()
                }
                R.id.industrial_commercial_search_question_10_answer_1_2 -> {
                    binding.industrialCommercialSearchQuestion10Answer2.visibility = View.VISIBLE
                    answer10 = "Sim;"
                }
            }
            Log.i("test->", answer10)
            viewModel.search10.postValue(viewModel.buildSearch(10, answer10))
        }

        binding.industrialCommercialSearchQuestion10Answer2.setOnClickListener(View.OnClickListener {
            answer10 = answer10 + binding.industrialCommercialSearchQuestion10Answer2
        })

        binding.industrialCommercialSearchQuestion11Answer1.setOnCheckedChangeListener { _, checkedId ->
            answer11 = when (checkedId) {
                R.id.industrial_commercial_search_question_11_answer_1_1 -> binding.industrialCommercialSearchQuestion11Answer11.text.toString()
                R.id.industrial_commercial_search_question_11_answer_1_2 -> binding.industrialCommercialSearchQuestion11Answer12.text.toString()
                R.id.industrial_commercial_search_question_11_answer_1_3 -> binding.industrialCommercialSearchQuestion11Answer13.text.toString()
                else -> ""
            }
            viewModel.search11.postValue(viewModel.buildSearch(11, answer11))
        }

         binding.industrialCommercialSearchQuestion12Answer1.setOnCheckedChangeListener { _, checkedId ->
            answer12 = when (checkedId) {
                R.id.industrial_commercial_search_question_12_answer_1_1 -> binding.industrialCommercialSearchQuestion12Answer11.text.toString()
                R.id.industrial_commercial_search_question_12_answer_1_2 -> binding.industrialCommercialSearchQuestion12Answer12.text.toString()
                R.id.industrial_commercial_search_question_12_answer_1_3 -> binding.industrialCommercialSearchQuestion12Answer13.text.toString()
                else -> ""
            }
            viewModel.search12.postValue(viewModel.buildSearch(12, answer12))
        }

        binding.industrialCommercialSearchQuestion13Answer1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.industrial_commercial_search_question_13_answer_1_1 -> {
                    binding.industrialCommercialSearchQuestion13Answer2.visibility = View.VISIBLE
                    answer13 = ""
                }
                R.id.industrial_commercial_search_question_13_answer_1_2 -> {
                    binding.industrialCommercialSearchQuestion13Answer2.visibility = View.GONE
                    answer13 = binding.industrialCommercialSearchQuestion13Answer12.text.toString()
                }
            }
            Log.i("test->", answer13)
            viewModel.search13.postValue(viewModel.buildSearch(13, answer13))
        }
        binding.industrialCommercialSearchQuestion13Answer2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.industrial_commercial_search_question_13_answer_2_1 -> {
                    answer13="Sim;" + binding.industrialCommercialSearchQuestion13Answer21.text.toString()
                }

                R.id.industrial_commercial_search_question_13_answer_2_2 -> {
                    answer13="Sim;" + binding.industrialCommercialSearchQuestion13Answer22.text.toString()
                }
                R.id.industrial_commercial_search_question_13_answer_2_3 -> {
                    answer13="Sim;" + binding.industrialCommercialSearchQuestion13Answer23.text.toString()
                }
            }
            Log.i("test->", answer13)
            viewModel.search13.postValue(viewModel.buildSearch(13, answer13))
        }

        binding.industrialCommercialSearchQuestion14Answer1.setOnCheckedChangeListener { _, checkedId ->
            answer14 = when (checkedId) {
                R.id.industrial_commercial_search_question_14_answer_1_1 -> binding.industrialCommercialSearchQuestion14Answer11.text.toString()
                R.id.industrial_commercial_search_question_14_answer_1_2 -> binding.industrialCommercialSearchQuestion14Answer12.text.toString()
                R.id.industrial_commercial_search_question_14_answer_1_3 -> binding.industrialCommercialSearchQuestion14Answer13.text.toString()
                else -> ""
            }
            viewModel.search14.postValue(viewModel.buildSearch(14, answer14))
        }

        binding.industrialCommercialSearchQuestion15Answer1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.industrial_commercial_search_question_15_answer_1_1 -> {
                    binding.industrialCommercialSearchQuestion15Answer2.visibility = View.VISIBLE
                    answer15 = ""
                }
                R.id.industrial_commercial_search_question_15_answer_1_2 -> {
                    binding.industrialCommercialSearchQuestion15Answer2.visibility = View.GONE
                    answer15 = binding.industrialCommercialSearchQuestion15Answer12.text.toString()
                }
            }
            Log.i("test->", answer15)
            viewModel.search15.postValue(viewModel.buildSearch(15, answer15))
        }
        binding.industrialCommercialSearchQuestion15Answer2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.industrial_commercial_search_question_15_answer_2_1 -> {
                    answer15="Sim;" + binding.industrialCommercialSearchQuestion15Answer21.text.toString()
                }
                R.id.industrial_commercial_search_question_15_answer_2_2 -> {
                    answer15="Sim;" + binding.industrialCommercialSearchQuestion15Answer22.text.toString()
                }
                R.id.industrial_commercial_search_question_15_answer_2_3 -> {
                    answer15="Sim;" + binding.industrialCommercialSearchQuestion15Answer23.text.toString()
                }
                R.id.industrial_commercial_search_question_15_answer_2_4 -> {
                    answer15="Sim;" + binding.industrialCommercialSearchQuestion15Answer24.text.toString()
                }
                R.id.industrial_commercial_search_question_15_answer_2_5 -> {
                    answer15="Sim;" + binding.industrialCommercialSearchQuestion15Answer25.text.toString()
                }
            }
            Log.i("test->", answer15)
            viewModel.search15.postValue(viewModel.buildSearch(15, answer15))
        }

        binding.industrialCommercialSearchQuestion16Answer1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.industrial_commercial_search_question_16_answer_1_1 -> {
                    binding.industrialCommercialSearchQuestion16Answer2.visibility = View.VISIBLE
                    answer16 = ""
                }
                R.id.industrial_commercial_search_question_16_answer_1_2 -> {
                    binding.industrialCommercialSearchQuestion16Answer2.visibility = View.GONE
                    answer16 = binding.industrialCommercialSearchQuestion16Answer12.text.toString()
                }
            }
            Log.i("test->", answer16)
            viewModel.search16.postValue(viewModel.buildSearch(16, answer16))
        }
        binding.industrialCommercialSearchQuestion16Answer2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.industrial_commercial_search_question_16_answer_2_1 -> {
                    answer16="Sim;" + binding.industrialCommercialSearchQuestion16Answer21.text.toString()
                }
                R.id.industrial_commercial_search_question_16_answer_2_2 -> {
                    answer16="Sim;" + binding.industrialCommercialSearchQuestion16Answer22.text.toString()
                }
                R.id.industrial_commercial_search_question_16_answer_2_3 -> {
                    answer16="Sim;" + binding.industrialCommercialSearchQuestion16Answer23.text.toString()
                }
            }
            Log.i("test->", answer16)
            viewModel.search16.postValue(viewModel.buildSearch(16, answer16))
        }

        binding.industrialCommercialSearchQuestion17Answer1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.industrial_commercial_search_question_17_answer_1_1 -> {
                    binding.industrialCommercialSearchQuestion17Answer2.visibility = View.VISIBLE
                    answer17 = ""
                }
                R.id.industrial_commercial_search_question_17_answer_1_2 -> {
                    binding.industrialCommercialSearchQuestion17Answer2.visibility = View.GONE
                    answer17 = binding.industrialCommercialSearchQuestion17Answer12.text.toString()
                }
            }
            Log.i("test->", answer17)
            viewModel.search17.postValue(viewModel.buildSearch(17, answer17))
        }
        binding.industrialCommercialSearchQuestion17Answer2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.industrial_commercial_search_question_17_answer_2_1 -> {
                    answer17="Sim;" + binding.industrialCommercialSearchQuestion17Answer21.text.toString()
                }
                R.id.industrial_commercial_search_question_17_answer_2_2 -> {
                    answer17="Sim;" + binding.industrialCommercialSearchQuestion17Answer22.text.toString()
                }
                R.id.industrial_commercial_search_question_17_answer_2_3 -> {
                    answer17="Sim;" + binding.industrialCommercialSearchQuestion17Answer23.text.toString()
                }
                R.id.industrial_commercial_search_question_17_answer_2_4 -> {
                    answer17="Sim;" + binding.industrialCommercialSearchQuestion17Answer24.text.toString()
                }
            }
            Log.i("test->", answer17)
            viewModel.search17.postValue(viewModel.buildSearch(17, answer17))
        }
        */
