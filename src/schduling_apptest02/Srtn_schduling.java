package schduling_apptest02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

class Srtn_schduling {
	make_processes new_ps; //랜덤 프로세스
	use_processor new_co; //생성 프로세서
	ArrayList<Integer> ready = new ArrayList<>(); // 대기열
	int count_p = 0;

	Srtn_schduling(make_processes tum,int p,int e){
		new_ps = tum; //프로세스 생성
		new_co = new use_processor(p,e); //프로세서 생성
		play();
	} 
	
	void play() {
		int t = 0;
		while(new_ps.processes_list.size() != count_p) { //모든 프로세스가 일을 완료하면 종료
			for(int i = 0;i<new_ps.processes_list.size();i++) { //해당 시간(t) 해당하는 at 찾고 대기열에 넣기
				if(new_ps.processes_list.get(i).time[0] == t) {
					ready.add(i);
				}
			}
			
			if(ready.size() > 1)
				left_bt_solt(); //우선 대기열을 bt 작은순 정렬
			
			if(ready.isEmpty()) { //대기열 비어있으면 프로세서는 하던거 함
				new_co.kepwork_co();
				
			}else { //대기열이 있다면 가능한 프로세서에 프로세스 진입
				if(new_co.is_canplay()) { //사용가능 프로세서가 있음
					while(new_co.is_canplay() && (ready.size() != 0)) {
						//프로세스에 임시 저장되있는 이전 실행정보를 불러옴
						int bt_tum = new_ps.processes_list.get(ready.get(0)).past_bt;
						int using_tum = new_ps.processes_list.get(ready.get(0)).core_using_time;
						new_co.enter_co(new_co.co_index.get(0),ready.get(0),bt_tum,using_tum); //프로세서에 진입 - 진입만 함(아직 일안함)
						ready.remove(0); //대기열에서 삭제
					}
					if(ready.size() > 0)
						left_bt_co_change(t); //프로세서 와 대기열 프로세스 비교
					new_co.kepwork_co(); //프로세스 일 시작
					
				}else { //이미 모든 프로세서가 일하는중 
					left_bt_co_change(t); //프로세서 와 대기열 프로세스 비교
					new_co.kepwork_co();
				}
			}
			
			is_finish(t);
			t++;
		}

		printing();
	}
	
	void left_bt_co_change(int t) { //프로세서에 들어가 있는 프로세스 bt와 대기열 프로세스 교체 
		for(int i = 0;i<new_co.all_processor.size();i++) { 
			int p_index = new_co.all_processor.get(i).processes_index;
			int co_left_bt = new_ps.processes_list.get(p_index).time[1] - new_co.all_processor.get(i).count_bt;
			int ready_left_bt = new_ps.processes_list.get(ready.get(0)).time[1] - new_ps.processes_list.get(ready.get(0)).past_bt;
			
			if(co_left_bt > ready_left_bt) { //프로세서에 프로세스 와 대기열 프로세스의 남은 bt 시간 비교
				System.out.println("t : "+t+" = "+(i+1)+" core ps_le_bt > " + "p"+(ready.get(0)+1));
				
				//코어의 프로세스 bt진행 시간, 코어 사용 시간을 다시 프로세스로 임시저장
				new_ps.processes_list.get(p_index).past_bt = new_co.all_processor.get(i).count_bt;
				new_ps.processes_list.get(p_index).core_using_time = new_co.all_processor.get(i).core_using_time;
				
				ready.add(p_index); //다시 대기열 진입
				new_co.clear_co(i); //해당 코어 리셋
				
				//프로세스에 임시 저장되있는 이전 실행정보를 불러옴
				int bt_tum = new_ps.processes_list.get(ready.get(0)).past_bt;
				int using_tum = new_ps.processes_list.get(ready.get(0)).core_using_time;
				new_co.enter_co(i,ready.get(0),bt_tum,using_tum); //프로세서에 진입 
				ready.remove(0); //대기열에서 삭제
			}
			if(ready.size() > 1)
				left_bt_solt(); //추가된 대기열 다시 정렬
		}
	}
	
//	void left_bt_solt() { //대기열을 남은 bt에 따라 정렬
//		for(int i = 0;i<ready.size()-1;i++) {
//			for(int j = i+1;j<ready.size();j++) {
//				int i_left_bt = new_ps.processes_list.get(ready.get(i)).time[1] - new_ps.processes_list.get(ready.get(i)).past_bt;
//				int j_left_bt = new_ps.processes_list.get(ready.get(j)).time[1] - new_ps.processes_list.get(ready.get(j)).past_bt;
//				if(i_left_bt > j_left_bt)
//					Collections.swap(ready,i,j);
//			}
//		}
//	}
	
	void left_bt_solt() {
		Collections.sort(ready, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				int o1_left_bt = new_ps.processes_list.get(o1).time[1] - new_ps.processes_list.get(o1).past_bt;
				int o2_left_bt = new_ps.processes_list.get(o2).time[1] - new_ps.processes_list.get(o2).past_bt;
				return Integer.compare(o1_left_bt, o2_left_bt);
				}
		});
	}
	
	
	void is_finish(int t) {
		for(int i = 0;i<new_co.all_processor.size();i++) { //종료된 프로세스 찾기
			int p = new_co.all_processor.get(i).processes_index;
			if(new_ps.processes_list.get(p).time[1] <= new_co.all_processor.get(i).count_bt) {
				int p_index = new_co.all_processor.get(i).processes_index;
				new_ps.processes_list.get(p_index).core_using_time = new_co.all_processor.get(i).core_using_time;
				
				culculate(t, new_ps.processes_list.get(p).time[1], p, new_co.all_processor.get(i).type);
				
				count_p++;
				new_co.clear_co(i);
			}
		}
	}
	
	void culculate(int t,int bt,int prosesses_index,char type) { //계산
		new_ps.processes_list.get(prosesses_index).time[3] = 
				t - new_ps.processes_list.get(prosesses_index).time[0]+1; //t- at = tt / t - wt
		
		
		int true_bt = new_ps.processes_list.get(prosesses_index).core_using_time;
		new_ps.processes_list.get(prosesses_index).time[2] =
				new_ps.processes_list.get(prosesses_index).time[3] - true_bt;
		
		double a = new_ps.processes_list.get(prosesses_index).time[3] / (double)bt;
		new_ps.processes_list.get(prosesses_index).ntt = Math.round(a*100)/100.0;
	}
	
	
	void printing() { //출력
		System.out.println("    at bt wt tt  ntt");
		for(int i = 0;i<new_ps.processes_list.size();i++)
			System.out.println("p"+ (i+1)+ "= "+Arrays.toString(new_ps.processes_list.get(i).time) +
					" "+new_ps.processes_list.get(i).ntt);
		
		for(int i = 0;i<new_co.all_processor.size();i++) {
			if(i != (new_co.all_processor.size())) {
				System.out.println(" ");
				System.out.print((i+1)+" "+new_co.all_processor.get(i).type+"_core scheduling time : ");
			}
			for(int j = 0;j<new_co.all_processor.get(i).time.size();j++) {
				System.out.print(new_co.all_processor.get(i).time.get(j) + " ");
			}
			System.out.println("");
			System.out.println(" >> use_electric : "+new_co.all_processor.get(i).electric);
			
		}
	}
	
	public make_processes give_processes() {
		return new_ps;
	}
	public use_processor give_core() {
		return new_co;
	}
	
	
}
