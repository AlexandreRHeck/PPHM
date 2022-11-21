package mge.mobile.pphm.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import mge.mobile.pphm.R
import mge.mobile.pphm.databinding.FragmentResidentialBinding
import mge.mobile.pphm.databinding.FragmentRuralBinding
import mge.mobile.pphm.util.ViewModelFactory

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class RuralFragment(private val viewModel: SearchViewModel) : Fragment() {
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
    /*
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
    private var answer12 = ""
    private var answer13 = ""
    private var answer14 = ""
    private var answer15 = ""
    private var answer16 = ""

    private var email = ""
    private var telefone = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRuralBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        this.lifecycle.addObserver(viewModel)

        binding.ruralSearchQuestion1Answer.addTextChangedListener {
            answer1 = it.toString()
            viewModel.search1.postValue(viewModel.buildSearch(1, answer1))
        }

        binding.ruralSearchQuestion2Answer1.setOnCheckedChangeListener { _, checkedId ->
            answer2 = when (checkedId) {
                R.id.rural_search_question_2_answer_1_1 -> binding.ruralSearchQuestion2Answer11.text.toString()
                R.id.rural_search_question_2_answer_1_2 -> binding.ruralSearchQuestion2Answer12.text.toString()
                else -> ""
            }
            viewModel.search2.postValue(viewModel.buildSearch(2, answer2))
        }

        binding.ruralSearchQuestion3Answer.addTextChangedListener {
            telefone = it.toString()
            Log.i("test->", it.toString())
            answer3 = telefone
            viewModel.search3.postValue(viewModel.buildSearch(3, answer3))
        }

        binding.ruralSearchQuestion4Answer.addTextChangedListener {
            answer4 = it.toString()
            viewModel.search4.postValue(viewModel.buildSearch(4, answer4))
        }

        binding.ruralSearchQuestion5Answer1.setOnCheckedChangeListener { _, checkedId ->
            answer5 = when (checkedId) {
                R.id.rural_search_question_5_answer_1_1 -> binding.ruralSearchQuestion5Answer11.text.toString()
                R.id.rural_search_question_5_answer_1_2 -> binding.ruralSearchQuestion5Answer12.text.toString()
                R.id.rural_search_question_5_answer_1_3 -> binding.ruralSearchQuestion5Answer13.text.toString()
                R.id.rural_search_question_5_answer_1_4 -> binding.ruralSearchQuestion5Answer14.text.toString()
                else -> ""
            }
            viewModel.search5.postValue(viewModel.buildSearch(5, answer5))
        }

        binding.ruralSearchQuestion6Answer1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rural_search_question_6_answer_1_1 -> {
                    binding.ruralSearchQuestion6Answer2.visibility = View.GONE
                    answer6 = binding.ruralSearchQuestion6Answer11.text.toString()
                }
                R.id.rural_search_question_6_answer_1_2 -> {
                    binding.ruralSearchQuestion6Answer2.visibility = View.GONE
                    answer6 = binding.ruralSearchQuestion6Answer12.text.toString()
                }
                R.id.rural_search_question_6_answer_1_3 -> {
                    binding.ruralSearchQuestion6Answer2.visibility = View.GONE
                    answer6 = binding.ruralSearchQuestion6Answer13.text.toString()
                }
                R.id.rural_search_question_6_answer_1_4 -> {
                    binding.ruralSearchQuestion6Answer2.visibility = View.GONE
                    answer6 = binding.ruralSearchQuestion6Answer14.text.toString()
                }
                R.id.rural_search_question_6_answer_1_5 -> {
                    binding.ruralSearchQuestion6Answer2.visibility = View.VISIBLE
                    //answer6 = "Outra;" + ""
                }
                else -> ""
            }
            viewModel.search6.postValue(viewModel.buildSearch(6, answer6))
        }

        binding.ruralSearchQuestion6Answer2.setOnClickListener(View.OnClickListener {
            answer6 = "Outra;" + binding.ruralSearchQuestion6Answer2.text.toString()
        })

        binding.ruralSearchQuestion7Answer1.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion7Answer1.isChecked){
                if(!answer7.contains("Manhã;")){
                    answer7 = "Manhã;" + answer7
                }
            }
            else{
                if(answer7.contains("Manhã;")){
                    answer7 = answer7.replace("Manhã;", "");
                }
            }

            viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
        })

        binding.ruralSearchQuestion7Answer2.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion7Answer2.isChecked){
                if(!answer7.contains("Tarde;")){
                    answer7 = answer7 + "Tarde;"
                }
            }
            else{
                if(answer7.contains("Tarde;")){
                    answer7 = answer7.replace("Tarde;", "");
                }
            }

            viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
        })

        binding.ruralSearchQuestion7Answer3.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion7Answer3.isChecked){
                if(!answer7.contains("Noite;")){
                    answer7 = answer7 + "Noite;"
                }
            }
            else{
                if(answer7.contains("Noite;")){
                    answer7 = answer7.replace("Noite;", "");
                }
            }

            viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
        })

        binding.ruralSearchQuestion7Answer4.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion7Answer4.isChecked){
                if(!answer7.contains("Madrugada;")){
                    answer7 = answer7 + "Madrugada"
                }
            }
            else{
                if(answer7.contains("Madrugada;")){
                    answer7 = answer7.replace("Madrugada;", "");
                }
            }

            viewModel.search7.postValue(viewModel.buildSearch(7, answer7))
        })

        binding.ruralSearchQuestion8Answer1.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion8Answer1.isChecked){
                answer8 = "Não;" + answer8
                binding.ruralSearchQuestion8Answer2.setEnabled(false)
                binding.ruralSearchQuestion8Answer2.setChecked(false)
                binding.ruralSearchQuestion8Answer3.setEnabled(false)
                binding.ruralSearchQuestion8Answer3.setChecked(false)
                binding.ruralSearchQuestion8Answer4.setEnabled(false)
                binding.ruralSearchQuestion8Answer4.setChecked(false)
                binding.ruralSearchQuestion8Answer5.setEnabled(false)
                binding.ruralSearchQuestion8Answer5.setChecked(false)
                binding.ruralSearchQuestion8Answer6.setEnabled(false)
                binding.ruralSearchQuestion8Answer6.setChecked(false)
                binding.ruralSearchQuestion8Answer7.setEnabled(false)
                binding.ruralSearchQuestion8Answer7.setChecked(false)
                binding.ruralSearchQuestion8Answer8.setEnabled(false)
                binding.ruralSearchQuestion8Answer8.setChecked(false)
            }
            else{
                if(answer8.contains("Não;")){
                    answer8 = answer8.replace("Não;", "");
                    binding.ruralSearchQuestion8Answer2.setEnabled(true)
                    binding.ruralSearchQuestion8Answer3.setEnabled(true)
                    binding.ruralSearchQuestion8Answer4.setEnabled(true)
                    binding.ruralSearchQuestion8Answer5.setEnabled(true)
                    binding.ruralSearchQuestion8Answer6.setEnabled(true)
                    binding.ruralSearchQuestion8Answer7.setEnabled(true)
                    binding.ruralSearchQuestion8Answer8.setEnabled(true)
                }
            }
            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.ruralSearchQuestion8Answer2.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion8Answer2.isChecked){
                answer8 = answer8 + "Energia Elétrica por placas solares;"
                binding.ruralSearchQuestion8Answer1.setEnabled(false)
            }
            else{
                if(answer8.contains("Energia Elétrica por placas solares;")){
                    answer8 = answer8.replace("Energia Elétrica por placas solares;", "");
                    if((!binding.ruralSearchQuestion8Answer3.isEnabled)&&
                            (!binding.ruralSearchQuestion8Answer4.isEnabled)&&
                            (!binding.ruralSearchQuestion8Answer5.isEnabled)&&
                            (!binding.ruralSearchQuestion8Answer6.isEnabled)&&
                            (!binding.ruralSearchQuestion8Answer7.isEnabled)&&
                            (!binding.ruralSearchQuestion8Answer8.isEnabled)){
                        binding.ruralSearchQuestion8Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.ruralSearchQuestion8Answer3.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion8Answer3.isChecked){
                answer8 = answer8 + "Energia Térmica (aquecimento) por placas solares;"
                binding.ruralSearchQuestion8Answer1.setEnabled(false)
            }
            else{
                if(answer8.contains("Energia Térmica (aquecimento) por placas solares;")){
                    answer8 = answer8.replace("Energia Térmica (aquecimento) por placas solares;", "");
                    if((!binding.ruralSearchQuestion8Answer2.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer4.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer5.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer6.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer7.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer8.isEnabled)) {
                        binding.ruralSearchQuestion8Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.ruralSearchQuestion8Answer4.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion8Answer4.isChecked){
                answer8 = answer8 + "Energia Elétrica por queima de combustível (gerador a diesel);"
                binding.ruralSearchQuestion8Answer1.setEnabled(false)
            }
            else{
                if(answer8.contains("Energia Elétrica por queima de combustível (gerador a diesel);")){
                    answer8 = answer8.replace("Energia Elétrica por queima de combustível (gerador a diesel);", "");
                    if((!binding.ruralSearchQuestion8Answer2.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer3.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer5.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer6.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer7.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer8.isEnabled)) {
                        binding.ruralSearchQuestion8Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.ruralSearchQuestion8Answer5.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion8Answer5.isChecked){
                answer8 = answer8 + "Gás Encanado (para aquecimento);"
                binding.ruralSearchQuestion8Answer1.setEnabled(false)
            }
            else{
                if(answer8.contains("Gás Encanado (para aquecimento);")){
                    answer8 = answer8.replace("Gás Encanado (para aquecimento);", "");
                    if((!binding.ruralSearchQuestion8Answer2.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer3.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer4.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer6.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer7.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer8.isEnabled)) {
                        binding.ruralSearchQuestion8Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.ruralSearchQuestion8Answer6.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion8Answer6.isChecked){
                answer8 = answer8 + "Gás Envasado (botijão) qu enão seja o de cozinha;"
                binding.ruralSearchQuestion8Answer1.setEnabled(false)
            }
            else{
                if(answer8.contains("Gás Envasado (botijão) qu enão seja o de cozinha;")){
                    answer8 = answer8.replace("Gás Envasado (botijão) qu enão seja o de cozinha;", "");
                    if((!binding.ruralSearchQuestion8Answer2.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer3.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer4.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer5.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer7.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer8.isEnabled)) {
                        binding.ruralSearchQuestion8Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.ruralSearchQuestion8Answer7.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion8Answer7.isChecked){
                answer8 = answer8 + "Quemia a lenha;"
                binding.ruralSearchQuestion8Answer1.setEnabled(false)
            }
            else{
                if(answer8.contains("Queima a lenha;")){
                    answer8 = answer8.replace("Queima a lenha;", "");
                    if((!binding.ruralSearchQuestion8Answer2.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer3.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer4.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer5.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer6.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer8.isEnabled)) {
                        binding.ruralSearchQuestion8Answer1.setEnabled(true)
                    }
                }
            }

            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.ruralSearchQuestion8Answer8.setOnClickListener(View.OnClickListener {
            if(binding.ruralSearchQuestion8Answer8.isChecked){
                answer8 = answer8 + "Outra;"
                binding.ruralSearchQuestion8Answer1.setEnabled(false)
                binding.ruralSearchQuestion8Answer9.visibility = View.VISIBLE
            }
            else{
                var TextoAux : String
                TextoAux = answer8.substring(answer8.indexOf("Outra;"), answer8.length)
                if(answer8.contains("Outra;")){
                    answer8 = answer8.replace(
                        TextoAux, "");
                    binding.ruralSearchQuestion8Answer9.setText("")
                    binding.ruralSearchQuestion8Answer9.visibility = View.GONE
                    if((!binding.ruralSearchQuestion8Answer2.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer3.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer4.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer5.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer6.isEnabled)&&
                        (!binding.ruralSearchQuestion8Answer7.isEnabled)) {
                        binding.ruralSearchQuestion8Answer1.setEnabled(true)
                    }
                }
            }
            viewModel.search8.postValue(viewModel.buildSearch(8, answer8))
        })

        binding.ruralSearchQuestion8Answer9.setOnClickListener(View.OnClickListener {
            answer8 = answer8 + binding.ruralSearchQuestion8Answer9.text.toString()
        })

        binding.ruralSearchQuestion9Answer1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rural_search_question_9_answer_1_1 -> {
                    binding.ruralSearchQuestion9Answer2.visibility = View.GONE
                    answer9 = binding.ruralSearchQuestion9Answer12.text.toString()
                }
                R.id.rural_search_question_9_answer_1_2 -> {
                    binding.ruralSearchQuestion9Answer2.visibility = View.VISIBLE
                    answer9 = "Sim;"
                }
            }
            Log.i("test->", answer9)
            viewModel.search9.postValue(viewModel.buildSearch(9, answer9))
        }

        binding.ruralSearchQuestion9Answer2.setOnClickListener(View.OnClickListener {
            answer9 = answer9 + binding.ruralSearchQuestion9Answer2
        })

        binding.ruralSearchQuestion10Answer1.setOnCheckedChangeListener { _, checkedId ->
            answer10 = when (checkedId) {
                R.id.rural_search_question_10_answer_1_1 -> binding.ruralSearchQuestion10Answer11.text.toString()
                R.id.rural_search_question_10_answer_1_2 -> binding.ruralSearchQuestion10Answer12.text.toString()
                R.id.rural_search_question_10_answer_1_3 -> binding.ruralSearchQuestion10Answer13.text.toString()
                else -> ""
            }
            viewModel.search10.postValue(viewModel.buildSearch(10, answer10))
        }

        binding.ruralSearchQuestion11Answer1.setOnCheckedChangeListener { _, checkedId ->
            answer11 = when (checkedId) {
                R.id.rural_search_question_11_answer_1_1 -> binding.ruralSearchQuestion11Answer11.text.toString()
                R.id.rural_search_question_11_answer_1_2 -> binding.ruralSearchQuestion11Answer12.text.toString()
                R.id.rural_search_question_11_answer_1_3 -> binding.ruralSearchQuestion11Answer13.text.toString()
                else -> ""
            }
            viewModel.search11.postValue(viewModel.buildSearch(11, answer11))
        }

        binding.ruralSearchQuestion12Answer1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rural_search_question_12_answer_1_1 -> {
                    binding.ruralSearchQuestion12Answer2.visibility = View.VISIBLE
                    answer12 = ""
                }
                R.id.rural_search_question_12_answer_1_2 -> {
                    binding.ruralSearchQuestion12Answer2.visibility = View.GONE
                    answer12 = binding.ruralSearchQuestion12Answer12.text.toString()
                }
            }
            Log.i("test->", answer12)
            viewModel.search12.postValue(viewModel.buildSearch(12, answer12))
        }
        binding.ruralSearchQuestion12Answer2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rural_search_question_12_answer_2_1 -> {
                    answer12="Sim;" + binding.ruralSearchQuestion12Answer21.text.toString()
                }

                R.id.rural_search_question_12_answer_2_2 -> {
                    answer12="Sim;" + binding.ruralSearchQuestion12Answer22.text.toString()
                }
                R.id.rural_search_question_12_answer_2_3 -> {
                    answer12="Sim;" + binding.ruralSearchQuestion12Answer23.text.toString()
                }
            }
            Log.i("test->", answer12)
            viewModel.search12.postValue(viewModel.buildSearch(12, answer12))
        }

        binding.ruralSearchQuestion13Answer1.setOnCheckedChangeListener { _, checkedId ->
            answer13 = when (checkedId) {
                R.id.rural_search_question_13_answer_1_1 -> binding.ruralSearchQuestion13Answer11.text.toString()
                R.id.rural_search_question_13_answer_1_2 -> binding.ruralSearchQuestion13Answer12.text.toString()
                R.id.rural_search_question_13_answer_1_3 -> binding.ruralSearchQuestion13Answer13.text.toString()
                else -> ""
            }
            viewModel.search13.postValue(viewModel.buildSearch(13, answer13))
        }

        binding.ruralSearchQuestion14Answer1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rural_search_question_14_answer_1_1 -> {
                    binding.ruralSearchQuestion14Answer2.visibility = View.VISIBLE
                    answer14 = ""
                }
                R.id.rural_search_question_14_answer_1_2 -> {
                    binding.ruralSearchQuestion14Answer2.visibility = View.GONE
                    answer14 = binding.ruralSearchQuestion14Answer12.text.toString()
                }
            }
            Log.i("test->", answer14)
            viewModel.search14.postValue(viewModel.buildSearch(14, answer14))
        }
        binding.ruralSearchQuestion14Answer2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rural_search_question_14_answer_2_1 -> {
                    answer14="Sim;" + binding.ruralSearchQuestion14Answer21.text.toString()
                }
                R.id.rural_search_question_14_answer_2_2 -> {
                    answer14="Sim;" + binding.ruralSearchQuestion14Answer22.text.toString()
                }
                R.id.rural_search_question_14_answer_2_3 -> {
                    answer14="Sim;" + binding.ruralSearchQuestion14Answer23.text.toString()
                }
                R.id.rural_search_question_14_answer_2_4 -> {
                    answer14="Sim;" + binding.ruralSearchQuestion14Answer24.text.toString()
                }
                R.id.rural_search_question_14_answer_2_5 -> {
                    answer14="Sim;" + binding.ruralSearchQuestion14Answer25.text.toString()
                }
            }
            Log.i("test->", answer14)
            viewModel.search14.postValue(viewModel.buildSearch(14, answer14))
        }

        binding.ruralSearchQuestion15Answer1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rural_search_question_15_answer_1_1 -> {
                    binding.ruralSearchQuestion15Answer2.visibility = View.VISIBLE
                    answer15 = ""
                }
                R.id.rural_search_question_15_answer_1_2 -> {
                    binding.ruralSearchQuestion15Answer2.visibility = View.GONE
                    answer15 = binding.ruralSearchQuestion15Answer12.text.toString()
                }
            }
            Log.i("test->", answer15)
            viewModel.search15.postValue(viewModel.buildSearch(15, answer15))
        }
        binding.ruralSearchQuestion15Answer2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rural_search_question_15_answer_2_1 -> {
                    answer15="Sim;" + binding.ruralSearchQuestion15Answer21.text.toString()
                }
                R.id.rural_search_question_15_answer_2_2 -> {
                    answer15="Sim;" + binding.ruralSearchQuestion15Answer22.text.toString()
                }
                R.id.rural_search_question_15_answer_2_3 -> {
                    answer15="Sim;" + binding.ruralSearchQuestion15Answer23.text.toString()
                }
            }
            Log.i("test->", answer15)
            viewModel.search15.postValue(viewModel.buildSearch(15, answer15))
        }

        binding.ruralSearchQuestion16Answer1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rural_search_question_16_answer_1_1 -> {
                    binding.ruralSearchQuestion16Answer2.visibility = View.VISIBLE
                    answer16 = ""
                }
                R.id.rural_search_question_16_answer_1_2 -> {
                    binding.ruralSearchQuestion16Answer2.visibility = View.GONE
                    answer16 = binding.ruralSearchQuestion16Answer12.text.toString()
                }
            }
            Log.i("test->", answer16)
            viewModel.search16.postValue(viewModel.buildSearch(16, answer16))
        }
        binding.ruralSearchQuestion16Answer2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rural_search_question_16_answer_2_1 -> {
                    answer16="Sim;" + binding.ruralSearchQuestion16Answer21.text.toString()
                }
                R.id.rural_search_question_16_answer_2_2 -> {
                    answer16="Sim;" + binding.ruralSearchQuestion16Answer22.text.toString()
                }
                R.id.rural_search_question_16_answer_2_3 -> {
                    answer16="Sim;" + binding.ruralSearchQuestion16Answer23.text.toString()
                }
                R.id.rural_search_question_16_answer_2_4 -> {
                    answer16="Sim;" + binding.ruralSearchQuestion16Answer24.text.toString()
                }
            }
            Log.i("test->", answer16)
            viewModel.search16.postValue(viewModel.buildSearch(16, answer16))
        }
        */
