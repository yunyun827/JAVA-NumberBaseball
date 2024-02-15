import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class NumberBaseballGame extends JFrame implements GameLogic {

    private List<Integer> answer;
    private JPanel panel = new JPanel();
    private JTextField inputField = new JTextField();
    private JTextArea resultArea = new JTextArea();
    private int remainingAttempts = 9;  // 남은 기회를 추적하는 변수

    public NumberBaseballGame() {//새로운 게임 생성
        initGame();//게임의 답을 설정한다 

        setTitle("숫자 야구 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI(); //UI 초기화
        startGame();//게임 시작 메시지 출력 
    }

    @Override
    public void initGame() {
        List<Integer> numbers = new ArrayList<>(); //숫자를 채워넣기 위한 배열 리스트 만들기 
        for (int i = 1; i <= 9; i++) {
            numbers.add(i); //1~9 중 숫자를 랜덤으로 넣기 
        }
        Collections.shuffle(numbers); //무작위로 중복되지 않게 숫자 섞기.  

        answer = numbers.subList(0, 3); // 정답을 지정시키기. 
    }

    @Override
    public void startGame() {
        resultArea.append("숫자 야구 게임을 시작합니다.\n1부터 9까지의 숫자 3개를 이용해 정답을 맞춰보세요.\n기회는 총 9번입니다!\n⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆게임 시작!⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆⋆\n");
    }

    @Override
    public void checkAnswer(String inputText) {
        if (remainingAttempts > 0) {
            String message; //출력할 메시지 변수 
            if (inputText.length() != 3 || !inputText.matches("[1-9]+")) { //1~9의 숫자로 이루어지지 않았거나 3자리 수가 아닌 경우 배제 
                message = "올바른 숫자를 입력하세요.\n";
                resultArea.append(message);
                
            } else {
                List<Integer> guess = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    guess.add(Character.getNumericValue(inputText.charAt(i)));
                }

                int strikes = 0;
                int balls = 0;

                for (int i = 0; i < 3; i++) {
                    if (guess.get(i).equals(answer.get(i))) {//답과 사용자 입력값이 자리, 숫자 모두 일치 
                        strikes++;//스트라이크+1
                    } else if (answer.contains(guess.get(i))) {//답과 사용자 입력값이 자리는 다르나 숫자 일치(답에 포함됨)
                        balls++;//볼 +1
                    }
                }

                if (strikes == 3) {//스트라이크 3개 = 모든 자리와 값이 맞음 
                    message = "정답입니다! 축하합니다.\n";//승리 메시지 출력 
                    inputField.setEditable(false);//입력 막음 
                } else {
                    message = String.format("입력: %s  | 결과: %d 스트라이크, %d 볼\n", inputText, strikes, balls);
                    //현 상태 출력 
                    inputField.setText("");//인풋 필드 초기화 
                }

                remainingAttempts--;// 기회 차감 
                resultArea.append(message);
                if (remainingAttempts == 0) {//기회 안에 못 맞춘 경우 
                    resultArea.append("게임 종료. 정답은 " + answer.toString() + "입니다.\n");//답 공개 
                    inputField.setEditable(false);//입력 불가능 
                } else {
                    resultArea.append("남은 기회: " + remainingAttempts + "번\n");//기회 남았을 때 남은 기회 보여줌 
                }
            }
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(resultArea);//스크롤 가능 
        panel.setLayout(new GridLayout(2, 1));//그리드 레이아웃 사용 
        inputField.setHorizontalAlignment(JTextField.CENTER);
        panel.add(inputField);
        panel.add(scrollPane);
        add(panel, BorderLayout.CENTER);

        // ActionListener 추가
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//사용자가 인풋에 숫자입력하고 엔터 누를 경우 checkAnswer실행. 
                checkAnswer(inputField.getText());
            }
        });
        pack();
        setSize(400, 200); //사이즈 지정 
    }

}
