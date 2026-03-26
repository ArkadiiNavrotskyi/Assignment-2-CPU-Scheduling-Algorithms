import java.util.*;

class Process {
    String id;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int turnAroundTime;
    int waitingTime;

    public Process(String id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

public class SchedulingApp {

    public static void main(String[] args) {
        // Case 1 Data
        List<Process> case1 = new ArrayList<>();
        case1.add(new Process("P1", 0, 8));
        case1.add(new Process("P2", 0, 4));
        case1.add(new Process("P3", 0, 2));
        case1.add(new Process("P4", 0, 6));
        case1.add(new Process("P5", 0, 3));

        System.out.println("--- CASE 1 RESULTS ---");
        runAll(case1);

        // Case 2 Data
        List<Process> case2 = new ArrayList<>();
        case2.add(new Process("P1", 0, 20));
        case2.add(new Process("P2", 1, 2));
        case2.add(new Process("P3", 2, 2));
        case2.add(new Process("P4", 3, 1));
        case2.add(new Process("P5", 4, 3));

        System.out.println("\n--- CASE 2 RESULTS ---");
        runAll(case2);

        // Case 3 Data
        List<Process> case3 = new ArrayList<>();
        case3.add(new Process("P1", 0, 20));
        case3.add(new Process("P2", 1, 2));
        case3.add(new Process("P3", 2, 2));
        case3.add(new Process("P4", 3, 2));
        case3.add(new Process("P5", 4, 2));
        case3.add(new Process("P6", 5, 2));

        System.out.println("\n--- CASE 3 RESULTS ---");
        runAll(case3);
    }

    public static void runAll(List<Process> data) {
        System.out.println("\n[FCFS]");
        printResult(fcfs(cloneList(data)));
        System.out.println("\n[SJF]");
        printResult(sjf(cloneList(data)));
        System.out.println("\n[SRTF]");
        printResult(srtf(cloneList(data)));
    }

    public static List<Process> fcfs(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        for (Process p : processes) {
            if (currentTime < p.arrivalTime) currentTime = p.arrivalTime;
            p.completionTime = currentTime + p.burstTime;
            p.turnAroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnAroundTime - p.burstTime;
            currentTime = p.completionTime;
        }
        return processes;
    }

    public static List<Process> sjf(List<Process> processes) {
        int n = processes.size();
        List<Process> result = new ArrayList<>();
        List<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;
        int completed = 0;
        boolean[] isDone = new boolean[n];

        while (completed < n) {
            for (int i = 0; i < n; i++) {
                if (!isDone[i] && processes.get(i).arrivalTime <= currentTime) {
                    readyQueue.add(processes.get(i));
                    isDone[i] = true;
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));
            Process p = readyQueue.remove(0);
            p.completionTime = currentTime + p.burstTime;
            p.turnAroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnAroundTime - p.burstTime;
            currentTime = p.completionTime;
            result.add(p);
            completed++;
        }
        return result;
    }

    public static List<Process> srtf(List<Process> processes) {
        int n = processes.size();
        int completed = 0;
        int currentTime = 0;
        Process currentP = null;

        while (completed < n) {
            Process shortest = null;
            int minRemaining = Integer.MAX_VALUE;

            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                    if (p.remainingTime < minRemaining) {
                        minRemaining = p.remainingTime;
                        shortest = p;
                    }
                }
            }

            if (shortest == null) {
                currentTime++;
                continue;
            }

            shortest.remainingTime--;
            currentTime++;

            if (shortest.remainingTime == 0) {
                completed++;
                shortest.completionTime = currentTime;
                shortest.turnAroundTime = shortest.completionTime - shortest.arrivalTime;
                shortest.waitingTime = shortest.turnAroundTime - shortest.burstTime;
            }
        }
        return processes;
    }

    public static void printResult(List<Process> processes) {
        System.out.println("ID\tArr\tBurst\tCT\tTAT\tWT");
        double totalTAT = 0, totalWT = 0;
        for (Process p : processes) {
            System.out.println(p.id + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" + p.completionTime + "\t" + p.turnAroundTime + "\t" + p.waitingTime);
            totalTAT += p.turnAroundTime;
            totalWT += p.waitingTime;
        }
        System.out.printf("Avg TAT: %.2f, Avg WT: %.2f\n", totalTAT / processes.size(), totalWT / processes.size());
    }

    public static List<Process> cloneList(List<Process> list) {
        List<Process> clone = new ArrayList<>();
        for (Process p : list) clone.add(new Process(p.id, p.arrivalTime, p.burstTime));
        return clone;
    }
}