package memoryallocationproject;

import java.util.ArrayList;
import java.util.List;

public class Memory extends javax.swing.JFrame {

    public Memory() {
        initComponents();
        setChoices();
        setLocationRelativeTo(null);

    }

    class Hole {

        int starting_address;
        int end_address;
        int hole_Size;
        int free_space;
        ArrayList<Process> pList;

        public Hole(int starting_address, int size) {
            this.starting_address = starting_address;
            this.hole_Size = size;
            this.end_address = starting_address + size;
            this.free_space = hole_Size;
            pList = new ArrayList<>();
        }

        public void add_process(Process p) {
            pList.add(p);
            free_space -= p.get_Size();
        }

        public void remove_process(int pN) {
            Process tem;
            for (int z = 0; z < pList.size(); z++) {
                if (pList.get(z).num == pN) {

                    free_space += pList.get(z).p_Size;
                    pList.remove(z);
                }

            }
        }

        public void set_free_space(int f) {
            this.end_address = f;
        }

        public int get_free_Space() {
            return this.free_space;
        }

        public void set_Start(int starting_address) {
            this.starting_address = starting_address;
        }

        public void set_end(int end_address) {
            this.end_address = this.starting_address + hole_Size;
        }

        public int get_Start() {
            return this.starting_address;
        }

        public int get_end() {
            return this.end_address;
        }

        public int get_Size() {
            return this.hole_Size;
        }

        public void set_size(int h) {
            this.hole_Size = h;
        }
    }

    class Process {

        int num;
        int starting_address;
        int end_address;
        int p_Size;

        public Process(int num, int size) {
            this.num = num;
            this.p_Size = size;

        }

        public void set_Start(int starting_address) {
            this.starting_address = starting_address;
        }

        public void set_end(int end_address) {
            this.end_address = this.starting_address + p_Size;
        }

        public int get_Start() {
            return this.starting_address;
        }

        public int get_end() {
            return this.end_address;
        }

        public int get_Size() {
            return this.p_Size;
        }

        public void set_size(int h) {
            this.p_Size = h;
        }

        public int get_number() {
            return this.num;
        }

        public void set_number(int n) {
            this.num = n;
        }
    }

    class WholeMemory {

        ArrayList<Hole> holes;

        public WholeMemory(ArrayList<Hole> holes) {
            this.holes = holes;
        }

        public ArrayList<Hole> get_holes() {
            return this.holes;
        }

        public void setHoles(ArrayList<Hole> h) {
            this.holes = h;
        }

        public void addHole(Hole h) {
            this.holes.add(h);
        }

        public void removeHole(Hole h) {
            this.holes.remove(h);
        }

    }

    public static ArrayList<Hole> holesList = new ArrayList<>();
    ArrayList<Process> processesList = new ArrayList<>();
    int processesNum;
    String allocation_method;
    WholeMemory mem;

    public void getData() {

        String num = processesNo.getText().toString();
        processesNum = Integer.parseInt(num);

        allocation_method = method.getSelectedItem().toString();

        processesList = new ArrayList<>();

        String hdata = holes.getText().toString();
        String h_lines[] = hdata.split("\\r?\\n");

        for (int i = 0; i < h_lines.length; i++) {
            String temp = h_lines[i];
            String[] values = temp.split(",");
            String h_start = values[0];
            String h_size = values[1];
            int pn = Integer.parseInt(h_start);
            int pa = Integer.parseInt(h_size);
            Hole h_temp = new Hole(pn, pa);
            holesList.add(h_temp);
        }

        String pdata = sizes.getText().toString();
        String lines[] = pdata.split("\\r?\\n");

        for (int i = 0; i < lines.length; i++) {
            String temp = lines[i];
            String[] values = temp.split(",");
            String p_num = values[0];
            String p_size = values[1];
            int pn = Integer.parseInt(p_num);
            int pa = Integer.parseInt(p_size);
            Process p_temp = new Process(pn, pa);
            processesList.add(p_temp);
        }

    }

    public void allocate() {
        if ("First Fit".equals(allocation_method)) {
            firstFit();
        } else {
            bestFit();
        }
        mem = new WholeMemory(holesList);
    }

    public void firstFit() {
        for (int i = 0; i < processesNum; i++) {
            Process p_curr = processesList.get(i);
            int hole_index = searchHole(p_curr.get_Size());
            if (hole_index > -1) {
                holesList.get(hole_index).add_process(p_curr);
            }
        }
    }

    public void bestFit() {
        sortHoles(holesList);
        firstFit();
    }

    public int searchHole(int space) {
        for (int i = 0; i < holesList.size(); i++) {
            Hole temp = holesList.get(i);
            if (temp.get_free_Space() >= space) {
                return i;
            }

        }
        return -1;
    }

    public int search_P_Hole(int pNum) {
        for (int i = 0; i < holesList.size(); i++) {
            Hole temp = holesList.get(i);
            ArrayList<Process> tempP = temp.pList;
            for (int j = 0; j < tempP.size(); j++) {
                Process tt = tempP.get(j);
                if (tt.get_number() == pNum) {
                    return i;
                }
            }

        }
        return -1;
    }

    public void sortHoles(ArrayList<Hole> p) {

        for (int i = 0; i < p.size() - 1; i++) {
            for (int j = (i + 1); j < (p.size()); j++) {
                if (p.get(j).get_free_Space() < p.get(i).get_free_Space()) {
                    Hole temp = p.get(i);
                    p.set(i, p.get(j));
                    p.set(j, temp);
                }
            }
        }

    }

    public final void setChoices() {
        method.add("First Fit");
        method.add("Best Fit");

    }

    public void deallocate() {
        int d_process = Integer.parseInt(p_deallocate.getText().toString());
        int holeD = search_P_Hole(d_process);
        mem.get_holes().get(holeD).remove_process(d_process);
        processesNum--;
        
    }
    
    public void showOutput()
    {
        output.setText(" Memory would be like : ");
        output.append("\n");
        for (int i = 0 ; i<holesList.size();i++)
        {
                        output.append("\n");

            output.append("Start of hole\n");
            Hole temphole=holesList.get(i);
            output.append(temphole.starting_address+" :  ");
               output.append("\n");
            
            for (int j = 0 ;j<temphole.pList.size();j++ )
            {
                Process tempProcess=temphole.pList.get(j);
                output.append("Process "+tempProcess.num+" : size is "+tempProcess.get_Size());
             
            }
                           output.append("\n");

                 output.append("End of hole\n");
            output.append(temphole.end_address+"");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        processesNo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        holes = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        sizes = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        submit = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        output = new javax.swing.JTextArea();
        method = new java.awt.Choice();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        p_deallocate = new javax.swing.JTextField();
        Deallocation = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Calisto MT", 3, 24)); // NOI18N
        jLabel1.setText("Memory Allocation");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel2.setText("Holes :");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel3.setText("No of processes :");

        holes.setColumns(20);
        holes.setRows(5);
        jScrollPane1.setViewportView(holes);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel4.setText("Processes Size :");

        sizes.setColumns(20);
        sizes.setRows(5);
        jScrollPane2.setViewportView(sizes);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel5.setText("Allocation Method :");

        submit.setText("Submit");
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });

        output.setBackground(new java.awt.Color(204, 204, 204));
        output.setColumns(20);
        output.setRows(5);
        jScrollPane3.setViewportView(output);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel6.setText("Deallocate a process");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel7.setText("Process Num :");

        Deallocation.setText("Deallocate");
        Deallocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeallocationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(245, 245, 245))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(processesNo, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(155, 155, 155)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(method, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(p_deallocate, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 136, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(submit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Deallocation, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(processesNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(97, 97, 97)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(method, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(p_deallocate, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(Deallocation, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
        getData();
        allocate();
        showOutput();
    }//GEN-LAST:event_submitActionPerformed

    private void DeallocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeallocationActionPerformed
        deallocate();
    }//GEN-LAST:event_DeallocationActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Memory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Memory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Memory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Memory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Memory().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Deallocation;
    private javax.swing.JTextArea holes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private java.awt.Choice method;
    private javax.swing.JTextArea output;
    private javax.swing.JTextField p_deallocate;
    private javax.swing.JTextField processesNo;
    private javax.swing.JTextArea sizes;
    private javax.swing.JButton submit;
    // End of variables declaration//GEN-END:variables
}
