using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace Massage.Models
{
    public class Rezervacije
    {
        public int Id { get; set; }
        public DateTime DatumRezervacije { get; set; }
        [ForeignKey("UserID")]
        public long UserId { get; set; }
        public Korisnik User { get; set; }
        [ForeignKey("TerminID")]
        public int TerminId { get; set; }
        public Termini Termin { get; set; }
        public int Dan { get; set; }
        public int Mjesec { get; set; }
        public int Godina { get; set; }
    }
}
